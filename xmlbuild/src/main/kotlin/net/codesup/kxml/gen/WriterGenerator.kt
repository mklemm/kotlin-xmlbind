package net.codesup.kxml.gen

import com.sun.xml.xsom.XSComplexType
import com.sun.xml.xsom.XSFacet
import com.sun.xml.xsom.XSSimpleType
import com.sun.xml.xsom.parser.XSOMParser
import net.codesup.emit.*
import net.codesup.emit.declaration.ClassDeclaration
import org.xml.sax.InputSource
import java.net.URI
import java.nio.file.Path
import javax.xml.XMLConstants
import javax.xml.parsers.SAXParserFactory
import javax.xml.stream.XMLStreamWriter

/**
 * @author Mirko Klemm 2025-09-09
 *
 */
class WriterGenerator(val schemaFiles: List<InputSource>, val relevantNamespaces: Set<String>, val settings: XmlBuilderSettings = XmlBuilderSettings()) {
    val saxParserFactory = SAXParserFactory.newInstance().apply {
        isNamespaceAware = true
        setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
    }

    fun generateBuilders(outputDir: Path) {
        val parser = XSOMParser(saxParserFactory)
        parser.errorHandler = StdoutErrorHandler()
        schemaFiles.forEach {
            parser.parse(it)
        }
        val schemaSet = parser.result

        val typeMapper = TypeMapper(schemaSet.schemas.filter { it.targetNamespace in relevantNamespaces }
            .flatMap { it.simpleTypes.values }.map { simpleType ->
            val packageName = createPackageName(simpleType.targetNamespace)
            val className = createClassName(simpleType.name)
            if (simpleType.isRestriction && simpleType.asRestriction().declaredFacets.any { it.name == XSFacet.FACET_ENUMERATION })
                SimpleType(
                    QName(simpleType.targetNamespace, simpleType.name),
                    className(packageName, className),
                    enumFormatter
                )
            else
                SimpleType(
                    QName(simpleType.targetNamespace, simpleType.name),
                    className(String::class),
                    stringFormatter
                )
        })

        context(typeMapper, settings) {
            sourceBuilder {
                schemaSet.schemas.filter { it.targetNamespace in relevantNamespaces }
                    .forEach { schema ->
                        _package(createPackageName(schema.targetNamespace)) {
                            schema.simpleTypes.forEach { (name, simpleType) ->
                                if (simpleType.isRestriction && simpleType.asRestriction().declaredFacets.any { it.name == XSFacet.FACET_ENUMERATION }) {
                                    val typeDef = typeMapper.mapSimpleType(simpleType.targetNamespace, simpleType.name)
                                    _file(typeDef.className.localPart) {
                                        generateSimpleType(typeDef.className.localPart, simpleType)
                                    }
                                }
                            }
                            schema.complexTypes.forEach { (name, complexType) ->
                                val builderName = createBuilderName(complexType.name)
                                _file(builderName) {
                                    generateComplexType(builderName, complexType)
                                }
                            }
                            schema.elementDecls.filterValues { it.type.name !in schema.complexTypes.keys }
                                .forEach { (name, elementDecl) ->
                                    val builderName = createBuilderName(elementDecl.name)
                                    _file(builderName) {
                                        val classDef =
                                            generateComplexType(
                                                builderName,
                                                elementDecl.type as XSComplexType
                                            )

                                        _fun(createMemberName(elementDecl.name)) {
                                            val writerParam = param("xmlWriter") {
                                                type(sourceBuilder.typeUse(XMLStreamWriter::class))
                                            }
                                            val blockParam = param("block") {
                                                type {
                                                    receiver(classDef)
                                                }
                                            }
                                            block {
                                                st {
                                                    writerParam.call(
                                                        XMLStreamWriter::setDefaultNamespace,
                                                        str(elementDecl.targetNamespace)
                                                    )
                                                }
                                                st { writerParam.fn("writeStartDocument") }
                                                st {
                                                    writerParam.fn(
                                                        "writeStartElement",
                                                        str(elementDecl.targetNamespace),
                                                        str(elementDecl.name)
                                                    )
                                                }
                                                st {
                                                    writerParam.call(
                                                        XMLStreamWriter::writeDefaultNamespace,
                                                        str(elementDecl.targetNamespace)
                                                    )
                                                }
                                                st {
                                                    call(classDef) {
                                                        arg(writerParam)
                                                    }.call(blockParam)
                                                }
                                                st { v(writerParam).call(XMLStreamWriter::writeEndElement) }
                                                st { v(writerParam).call(XMLStreamWriter::writeEndDocument) }
                                            }
                                        }
                                    }
                                }
                        }
                    }
            }.generate(OutputContext(outputDir))
        }
    }
}

val strippedPrefixes = setOf("www", "w3", "smtp", "mail")
val wordStartCapitalsPattern = Regex("^(\\p{Upper}+)")

context (typeMapper: TypeMapper) fun SourceFile.generateSimpleType(className: String, simpleType: XSSimpleType) =
    _enum(className) {
        primaryConstructor {
            property("value") {
                val baseTypeMapped = typeMapper.mapSimpleType(
                    simpleType.asRestriction().baseType.targetNamespace,
                    simpleType.asRestriction().baseType.name
                )
                type(baseTypeMapped.className) {}
            }
        }
        simpleType.asRestriction().declaredFacets.filter { it.name == XSFacet.FACET_ENUMERATION }.forEach { facet ->
            _enumConstant(createClassName(facet.value.value)) {
                str(facet.value.value)
            }
        }
    }

context (typeMapper: TypeMapper, settings: XmlBuilderSettings) fun SourceFile.generateComplexType(
    builderName: String,
    complexType: XSComplexType
): ClassDeclaration {
    return _class(builderName) {
        primaryConstructor {
            property("xmlWriter") {
                type(sourceBuilder.typeUse(XMLStreamWriter::class))
            }
        }
        val writerProperty = primaryConstructor!!.parameters.first()
        complexType.attributeUses.forEach { au ->
            val memberName = createMemberName(au.decl.name)
            val simpleType = typeMapper.mapSimpleType(
                au.decl.type.targetNamespace,
                au.decl.type.name
            )
            val typeUse = sourceBuilder.typeUse(simpleType.className)
            val writeFunc = simpleType.writeFunc
            _fun(memberName) {
                val blockParam = param(memberName) {
                    type(typeUse)
                }
                block {
                    if (!au.decl.targetNamespace.isNullOrBlank()) {
                        st {
                            v(
                                writerProperty
                            ).fn(
                                "writeAttribute",
                                str(au.decl.targetNamespace),
                                str(au.decl.name),
                                writeFunc(blockParam)
                            )
                        }
                    } else {
                        st {
                            v(
                                writerProperty
                            ).fn(
                                "writeAttribute",
                                str(au.decl.name),
                                writeFunc(blockParam)
                            )
                        }
                    }
                }
            }
        }
        val particle = complexType.contentType.asParticle()
        particle?.term?.asModelGroup()?.children?.mapNotNull { child -> child.asParticle()?.term?.asElementDecl() }
            ?.forEach { elemDecl ->
                val memberName = createMemberName(elemDecl.name)
                if (elemDecl.type.isSimpleType) {
                    val simpleType = typeMapper.mapSimpleType(
                        elemDecl.type.targetNamespace,
                        elemDecl.type.name
                    )
                    val typeUse = sourceBuilder.typeUse(simpleType.className)
                    val writeFunc = simpleType.writeFunc
                    _fun(memberName) {
                        val blockParam = param(memberName) {
                            type(typeUse)
                        }
                        block {
                            st {
                                v(writerProperty).fn(
                                    "writeStartElement",
                                    str(elemDecl.targetNamespace),
                                    str(elemDecl.name)
                                )
                            }
                            st {
                                v(writerProperty).fn(
                                    "writeCharacters",
                                    writeFunc(blockParam)
                                )
                            }
                            st { v(writerProperty).fn("writeEndElement") }
                        }
                    }
                } else {
                    val type = sourceBuilder.externalType(
                        createBuilderName(
                            elemDecl.type.targetNamespace,
                            elemDecl.type.name
                        )
                    )
                    _fun(memberName) {
                        val blockParam = param("block") {
                            type {
                                receiver(type) {
                                    isNullable = elemDecl.isNillable
                                }
                            }
                        }
                        block {
                            st {
                                v(writerProperty).fn(
                                    "writeStartElement",
                                    str(elemDecl.targetNamespace),
                                    str(elemDecl.name)
                                )
                            }
                            st {
                                call(type)(v(writerProperty)).fn(
                                    "apply",
                                    v(blockParam)
                                )
                            }
                            st { v(writerProperty).fn("writeEndElement") }
                        }
                    }
                }
            }
    }
}


fun createPackageName(namespace: String): PackageName =
    packageName(URI.create(namespace).let { uri ->
        ((if (uri.scheme == "urn") uri.schemeSpecificPart.split(':') else uri.authority.split('.')
            .reversed().dropLastWhile { it in strippedPrefixes })
                + (uri.path?.split('/')?.filter { it.isNotBlank() } ?: emptyList()))
    })

fun createClassName(xsdName: String): String =
    xsdName.split('_', '-').joinToString("") { it.replaceFirstChar { it.uppercase() } }

fun createClassName(namespace: String, xsdName: String): ClassName =
    className(createPackageName(namespace), createClassName(xsdName))


fun createBuilderName(xsdName: String): String = createClassName(xsdName) + "Builder"

fun createBuilderName(namespace: String, xsdName: String): ClassName =
    className(createPackageName(namespace), createBuilderName(xsdName))

fun createMemberName(xsdName: String): String =
    xsdName.split('_', '-').let { l ->
        wordStartCapitalsPattern.replace(l.first()) { m -> m.groups[1]?.value?.lowercase() ?: l.first() } + l.drop(1)
            .joinToString("") { p -> p.replaceFirstChar { it.uppercase() } }
    }


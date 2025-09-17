package net.codesup.jaxb


import com.sun.xml.xsom.XSComplexType
import com.sun.xml.xsom.XSElementDecl
import com.sun.xml.xsom.XSParticle
import com.sun.xml.xsom.XSSchema
import net.codesup.emit.OutputContext
import net.codesup.emit.sourceBuilder
import net.codesup.tools.xkc.Options
import net.codesup.tools.xkc.model.logical.LogicalModel
import org.xml.sax.ErrorHandler
import java.math.BigInteger
import java.net.URI
import javax.xml.stream.XMLStreamWriter*

/**
 * @author Mirko Klemm 2021-03-24
 *
 */
class XmlBuilderGenerator(val outline: LogicalModel, val opt: Options, val errorHandler: ErrorHandler) {
    fun generate(): Boolean {
        val sourceBuilder = sourceBuilder {
            val streamWriterClass = typeUse(XMLStreamWriter::class)
            val uriClass = typeUse(URI::class)
            val staxBuilderPackage = "net.codesup.util.xml"
            outline.model.schemaComponent.schemas.forEach { schema ->
                schema.iterateElementDecls().forEach { el ->
                    val packageName = outline.model.nameConverter.toClassName(schema.targetNamespace)
                    val className = outline.model.nameConverter.toClassName(el.name)
                    _package(packageName) {
                        _file(className) {
                            _class(className) {
                                primaryConstructor {
                                    property("streamWriter") {
                                        type = streamWriterClass
                                    }
                                }
                                if (el.type.isComplexType) {
                                    val complexType = el.type as XSComplexType
                                    complexType.elementDecls.forEach { prop ->
                                        _fun(prop.kotlinName) {
                                            param("block") {
                                                type {
                                                    receiver(XMLStreamWriter::class)
                                                }
                                            }
                                            block {
                                                inv(staxBuilderPackage+".element") {
                                                    arg(uriClass.inv("create") {
                                                        arg(str(prop.targetNamespace))
                                                    })
                                                    arg(str(prop.kotlinName))
                                                    arg(v("block"))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        sourceBuilder.generate(OutputContext(opt.targetDir.toPath().resolve("../../../xkc/kotlin/main")))
        return true
    }

    val CPropertyInfo.isNullable
        get() = !isCollection
                && (isOptionalPrimitive
                || (schemaComponent is XSParticle && (schemaComponent as XSParticle).minOccurs == BigInteger.valueOf(0)))
                || (schemaComponent is XSElementDecl && (schemaComponent as XSElementDecl).isNillable)

    val CPropertyInfo.isLeaf get() = isOptionalPrimitive || baseType?.let { it.isPrimitive || it.isArray || it.fullName() == "java.lang.String" } ?: false

    fun XSSchema.getPackageName() = URI.create(targetNamespace).let { uri ->
        when (uri.scheme) {
            in listOf("http", "https", "ftp", "sftp") ->
                uri.host.substringAfter("www.").split('.').reversed() + '.' + uri.path.replace('/', '.')
            "urn" ->
                uri.path.replace(':', '.')
            else -> uri.path.replace('/', '.')
        }
    }

    val XSElementDecl.kotlinName get() = outline.model.nameConverter.toVariableName(name)

}

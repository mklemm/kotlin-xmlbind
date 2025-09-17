package net.codesup.tools.xkc.generator.poko

import com.sun.codemodel.*
import com.sun.tools.xjc.Options
import com.sun.tools.xjc.model.*
import net.codesup.emit.*
import net.codesup.emit.declaration.*
import net.codesup.jaxb.types.primitiveTypeNeutralLiterals
import net.codesup.jaxb.types.toKotlin
import net.codesup.emit.use.ClassTypeUse

/**
 * @author Mirko Klemm 2021-12-20
 *
 */
class BuilderGenerator(val model: Model) {
    fun generateProperties(opt: Options) {
        val outputContext = OutputContext(opt.targetDir.toPath().resolve("../../../xkc/kotlin/main"))
        sourceBuilder {
            model.codeModel.packages().forEach { pkg ->
                _package(pkg.name()) {
                    val classesToRemove = mutableListOf<JClass>()
                    pkg.classes().forEach { cls ->
                        findBean(cls)?.let { bean ->
                            _file(cls.name()) {
                                val iface = generateInterface(cls, bean)
                                val iFaceUse = ClassTypeUse(packageName.qualifiedName.resolve(iface.name))
                                generateBuilder(cls, bean, iFaceUse)
                                generateImmutable(cls, bean, iFaceUse)
                            }
                            classesToRemove.add(cls)
                        }
                    }
                    classesToRemove.forEach { pkg.remove(it) }
                    pkg.classes().asSequence().singleOrNull { it.name() == "ObjectFactory" }?.let { pkg.remove(it) }
                }
            }
            generate(outputContext)
        }
    }

    private fun <T : Declaration<T>> DeclarationOwner<T>.generateBuilder(
        cls: JDefinedClass,
        bean: CClassInfo,
        implements: ClassTypeUse?
    ) {
        _class(cls.name().toBuilderName()) {
            cls.annotations().forEach { ja ->
                annotate(ja.toKotlin())
            }
            if (bean.hasSubClasses()) {
                isOpen()
            }
            if (cls._extends() != null && cls._extends().erasure()
                    .fullName() != "java.lang.Object"
            ) {
                superType(cls._extends().toKotlin().toBuilderName()) {
                    cons()
                }
            }
            if (implements != null) {
                superType(implements)
            }
            cls._implements().forEach {
                superType(it.toKotlin())
            }
            primaryConstructor {}
            cls.fields().forEach { (name, fieldVar) ->
                val propertyInfo =
                    bean.properties?.firstOrNull {
                        it.getName(false) == name
                    }
                if (propertyInfo != null) {
                    val isPropertyNullable =
                        propertyInfo.isNullable && !propertyInfo.isCollection
                    val isElementNullable = propertyInfo.isNullable
                    _property(name) {
                        if (implements != null) {
                            modifier(PropertyModifier.OVERRIDE)
                        }
                        isMutable = true
                        if (isPropertyNullable) {
                            init {
                                lit("null")
                            }
                        } else if (propertyInfo.isCollection) {
                            init {
                                lit("mutableListOf()")
                            }
                        } else if (fieldVar.type().isPrimitive) {
                            init {
                                lit(
                                    primitiveTypeNeutralLiterals[fieldVar.type().name()]
                                        ?: "0"
                                )
                            }
                        } else {
                            isLateInit()
                        }
                        type(
                            fieldVar.type().toKotlin()
                        ) {
                            this.isNullable = isPropertyNullable
                        }
                        fieldVar.annotations().forEach { ja ->
                            annotate(ja.toKotlin())
                        }
                    }
                }
            }
            cls.classes().forEach { innerClass ->
                generateBuilder(innerClass, findBean(innerClass)!!, innerClass.toKotlin().toInterfaceName())
            }
        }
    }

    private fun <T : Declaration<T>> DeclarationOwner<T>.generateInterface(
        cls: JDefinedClass,
        bean: CClassInfo
    ): ClassDeclaration = _interface(cls.name().toInterfaceName()) {
        if (cls._extends() != null && cls._extends().erasure()
                .fullName() != "java.lang.Object"
        ) {
            superType(cls._extends().toKotlin().toInterfaceName())
        }
        cls._implements().forEach {
            superType(it.toKotlin())
        }
        cls.fields().forEach { (name, fieldVar) ->
            val propertyInfo =
                bean.properties?.firstOrNull {
                    it.getName(false) == name
                }
            if (propertyInfo != null) {
                val isPropertyNullable =
                    propertyInfo.isNullable && !propertyInfo.isCollection
                val isElementNullable = propertyInfo.isNullable
                _property(name) {
                    isMutable = false
                    type(
                        fieldVar.type().toKotlin()
                    ) {
                        this.isNullable = isPropertyNullable
                    }

                }
            }
        }
        cls.classes().forEach { innerClass ->
            generateInterface(innerClass, findBean(innerClass)!!)
        }
    }

    private fun findBean(cls: JClass) = model.beans().values.firstOrNull { it.fullName() == cls.erasure().fullName() }

    private fun <T : Declaration<T>> DeclarationOwner<T>.generateImmutable(
        cls: JDefinedClass,
        bean: CClassInfo,
        implements: ClassTypeUse
    ): ClassDeclaration = _class(cls.name()) {
        if (bean.hasSubClasses()) {
            isOpen()
        }
        if (cls._extends() != null && cls._extends().erasure()
                .fullName() != "java.lang.Object"
        ) {
            superType(cls._extends().toKotlin()) {
                cons {
                    generateSuperConstructorInvocation(cls, bean)
                }
            }
        }
        superType(implements)
        cls._implements().forEach {
            superType(it.toKotlin())
        }
        primaryConstructor {
            generateSuperConstructorParams(cls, bean)
            cls.fields().forEach { (name, fieldVar) ->
                val propertyInfo =
                    bean.properties?.firstOrNull {
                        it.getName(false) == name
                    }
                if (propertyInfo != null) {
                    val isPropertyNullable =
                        propertyInfo.isNullable && !propertyInfo.isCollection
                    val isElementNullable = propertyInfo.isNullable
                    property(name) {
                        isMutable = false
                        modifier(PropertyModifier.OVERRIDE)
                        type(
                            fieldVar.type().toKotlin()
                        ) {
                            this.isNullable = isPropertyNullable
                        }
                    }
                }
            }
        }
        cls.classes().forEach { innerClass ->
            generateImmutable(innerClass, findBean(innerClass)!!, innerClass.toKotlin().toInterfaceName())
        }
    }

    private fun PrimaryConstructorDeclaration.generateSuperConstructorParams(cls: JClass, bean: CClassInfo) {
        if (cls._extends() != null && model.containsClass(
                qn(
                    cls._extends().fullName()
                )
            ) && cls._extends() is JDefinedClass
        ) {
            val superClass = cls._extends() as JDefinedClass
            generateSuperConstructorParams(superClass, findBean(superClass)!!)
            superClass.fields().forEach { (name, fieldVar) ->
                val propertyInfo =
                    findBean(superClass)?.properties?.firstOrNull {
                        it.getName(false) == name
                    }
                if (propertyInfo != null) {
                    val isPropertyNullable =
                        propertyInfo.isNullable && !propertyInfo.isCollection
                    val isElementNullable = propertyInfo.isNullable
                    param(name) {
                        type(
                            fieldVar.type().toKotlin()
                        ) {
                            this.isNullable = isPropertyNullable
                        }
                    }
                }
            }
        }
    }

    private fun ConstructorInv.generateSuperConstructorInvocation(cls: JClass, bean: CClassInfo) {
        if (cls._extends() != null && model.containsClass(
                qn(
                    cls._extends().fullName()
                )
            ) && cls._extends() is JDefinedClass
        ) {
            val superClass = cls._extends() as JDefinedClass
            generateSuperConstructorInvocation(superClass, findBean(superClass)!!)
            superClass.fields().forEach { (name, fieldVar) ->
                val propertyInfo =
                    findBean(superClass)?.properties?.firstOrNull {
                        it.getName(false) == name
                    }
                if (propertyInfo != null) {
                    val isPropertyNullable =
                        propertyInfo.isNullable && !propertyInfo.isCollection
                    val isElementNullable = propertyInfo.isNullable
                    arg(context.v(name))
                }
            }
        }
    }

    private fun Model.containsClass(qualifiedName: QualifiedName) =
        beans().values.any { it.fullName() == qualifiedName.stringValue }

    private val CPropertyInfo.isNotNull: Boolean
        get() = this is CAttributePropertyInfo && this.isRequired
                || this is CElementPropertyInfo && (this.isRequired && this.types.none { it.isNillable }
                || this.isCollection)

    private val CPropertyInfo.isNullable: Boolean get() = !isNotNull

    private fun ClassTypeUse.toBuilderName() = mangleName("", "Builder")
    private fun String.toBuilderName() = this+"Builder"

    private fun QualifiedName.mangleName(prefix: String, suffix: String): QualifiedName =
        if (model.containsClass(this))
            QualifiedName(qualifier?.mangleName(prefix, suffix) ?: qualifier, prefix + localPart + suffix)
        else
            this

    private fun ClassTypeUse.mangleName(prefix: String, suffix: String): ClassTypeUse =
        copy(qualifiedName.mangleName(prefix, suffix)) {
            val newTypeArgs = typeArguments.map { ta ->
                if (ta is ClassTypeUse && model.containsClass(ta.qualifiedName)) {
                    ta.mangleName(prefix, suffix)
                } else {
                    ta
                }
            }
            typeArguments.clear()
            typeArguments.addAll(newTypeArgs)
        }


    private fun ClassTypeUse.toInterfaceName() = mangleName("I", "")
    private fun String.toInterfaceName() = "I$this"

}

package net.codesup.tools.xkc.generator.poko

import com.sun.codemodel.*
import com.sun.tools.xjc.generator.annotation.spec.XmlAccessorTypeWriter
import com.sun.tools.xjc.model.CClassInfo
import com.sun.tools.xjc.outline.Aspect
import com.sun.tools.xjc.outline.Outline
import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlEnum
import jakarta.xml.bind.annotation.XmlEnumValue
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.declaration.FunctionDeclaration
import net.codesup.emit.declaration.KDocBuilder
import net.codesup.emit.declaration.ParameterDeclaration
import net.codesup.emit.use.TypeUse

/**
 * Decides how a bean token is mapped to the generated classes.
 *
 *
 *
 * The actual implementations of this interface is tightly coupled with
 * the backend, but the front-end gets to choose which strategy to be used.
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
@XmlEnum(Boolean::class)
enum class ImplStructureStrategy {
    /**
     * Generates beans only. The simplest code generation.
     */
    @XmlEnumValue("true")
    BEAN_ONLY {
        override fun createClasses(outline: Outline, bean: CClassInfo): Result {
            val parent = outline.getContainer(bean.parent(), Aspect.EXPOSED)
            val impl = outline.classFactory.createClass(
                parent,
                JMod.PUBLIC or (if (parent.isPackage) 0 else JMod.STATIC) or if (bean.isAbstract) JMod.ABSTRACT else 0,
                bean.shortName, bean.locator
            )
            impl.annotate2(XmlAccessorTypeWriter::class.java).value(XmlAccessType.FIELD)
            return Result(impl, impl)
        }

        override fun getPackage(pkg: JPackage, a: Aspect): JPackage {
            return pkg
        }

        override fun createMethodWriter(target: ClassOutlineImpl): MethodWriter {
            assert(target.ref === target.implClass)
            return object : MethodWriter(target) {
                private val impl = target.implClass
                private var implMethod: JMethod? = null
                override fun addParameter(type: TypeUse, name: String): ParameterDeclaration {
                    return implMethod.param(type, name)
                }

                override fun declareMethod(returnType: TypeUse, methodName: String): FunctionDeclaration {
                    implMethod = impl.method(JMod.PUBLIC, returnType, methodName)
                    return implMethod
                }

                override fun javadoc(): KDocBuilder {
                    return implMethod!!.javadoc()
                }
            }
        }

        override fun _extends(derived: ClassOutlineImpl, base: ClassOutlineImpl) {
            derived.implClass._extends(base.implRef)
        }
    },

    /**
     * Generates the interfaces to describe beans (content interfaces)
     * and then the beans themselves in a hidden impl package.
     *
     * Similar to JAXB 1.0.
     */
    @XmlEnumValue("false")
    INTF_AND_IMPL {
        override fun createClasses(outline: Outline, bean: CClassInfo): Result {
            var parent = outline.getContainer(bean.parent(), Aspect.EXPOSED)
            val intf = outline.classFactory.createInterface(
                parent, bean.shortName, bean.locator
            )
            parent = outline.getContainer(bean.parent(), Aspect.IMPLEMENTATION)
            val impl = outline.classFactory.createClass(
                parent,
                JMod.PUBLIC or (if (parent.isPackage) 0 else JMod.STATIC) or if (bean.isAbstract) JMod.ABSTRACT else 0,
                bean.shortName + "Impl", bean.locator
            )
            impl.annotate2(XmlAccessorTypeWriter::class.java).value(XmlAccessType.FIELD)
            impl._implements(intf)
            return Result(intf, impl)
        }

        override fun getPackage(pkg: JPackage, a: Aspect): JPackage {
            return when (a) {
                Aspect.EXPOSED -> pkg
                Aspect.IMPLEMENTATION -> pkg.subPackage("impl")
                else -> {
                    assert(false)
                    throw IllegalStateException()
                }
            }
        }

        override fun createMethodWriter(target: ClassOutlineImpl): MethodWriter {
            return object : MethodWriter(target) {
                private val intf = target.ref
                private val impl = target.implClass
                private var intfMethod: JMethod? = null
                private var implMethod: JMethod? = null
                override fun addParameter(type: TypeUse, name: String): ParameterDeclaration {
                    // TODO: do we still need to deal with the case where intf is null?
                    if (intf != null) intfMethod!!.param(type, name)
                    return implMethod!!.param(type, name)
                }

                override fun declareMethod(returnType: TypeUse, methodName: String): FunctionDeclaration {
                    if (intf != null) intfMethod = intf.method(0, returnType, methodName)
                    implMethod = impl.method(JMod.PUBLIC, returnType, methodName)
                    return implMethod
                }

                override fun javadoc(): KDocBuilder {
                    return if (intf != null) intfMethod!!.javadoc() else implMethod!!.javadoc()
                }
            }
        }

        protected override fun _extends(derived: ClassOutlineImpl, base: ClassOutlineImpl) {
            derived.implClass._extends(base.implRef)
            derived.ref._implements(base.ref)
        }
    };

    /**
     * Creates class(es) for the given bean.
     */
    protected abstract fun createClasses(outline: Outline, bean: CClassInfo): Result

    /**
     * Gets the specified aspect of the given package.
     */
    protected abstract fun getPackage(pkg: JPackage, a: Aspect): JPackage
    protected abstract fun createMethodWriter(target: ClassOutlineImpl): MethodWriter

    /**
     * Sets up an inheritance relationship.
     */
    protected abstract fun _extends(derived: ClassOutlineImpl, base: ClassOutlineImpl)
    class Result(
        /**
         * Corresponds to [Aspect.EXPOSED]
         */
        val exposed: ClassDeclaration,
        /**
         * Corresponds to [Aspect.IMPLEMENTATION]
         */
        val implementation: ClassDeclaration
    )
}

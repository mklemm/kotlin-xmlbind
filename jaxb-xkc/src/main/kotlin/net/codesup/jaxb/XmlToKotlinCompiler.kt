package net.codesup.jaxb

import com.sun.tools.xjc.Options
import com.sun.tools.xjc.Plugin
import com.sun.tools.xjc.model.CPropertyInfo
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Outline
import com.sun.xml.xsom.XSElementDecl
import com.sun.xml.xsom.XSParticle
import net.codesup.tools.xkc.generator.poko.BuilderGenerator
import net.codesup.emit.QualifiedName
import net.codesup.emit.sourceBuilder
import org.xml.sax.ErrorHandler
import java.math.BigInteger

/**
 * @author Mirko Klemm 2021-03-23
 *
 */
class XmlToKotlinCompiler {
    fun getUsage(): String = "-Xkt: Generate Kotlin code."

    fun run(outline: Outline, opt: Options, errorHandler: ErrorHandler): Boolean {
        BuilderGenerator(outline.model).generateProperties(opt)
        return false
    }

    override fun postProcessModel(model: Model, errorHandler: ErrorHandler) {
        model.codeModel.addClassNameReplacer("^((?:\\w+.)+)(\\w+)","$1$2Builder")
    }

    fun testFunc(outline: Outline, opt: Options, errorHandler: ErrorHandler) {
        val sourceBuilder = sourceBuilder {
                   outline.classes.forEach { cls ->
                       _package(cls._package()._package().name()) {
                           _file(cls.target.shortName) {
                               _class(cls.target.shortName) {
                                   primaryConstructor {
                                       cls.target.properties.forEach { prop ->
                                           property(prop.getName(false)) {
                                               type(QualifiedName(prop.baseType.name())) {
                                                   isNullable = prop.isNullable
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

    val CPropertyInfo.isNullable get() =  !isCollection
            && (isOptionalPrimitive
            || (schemaComponent is XSParticle && (schemaComponent as XSParticle).minOccurs == BigInteger.valueOf(0)))
            || (schemaComponent is XSElementDecl && (schemaComponent as XSElementDecl).isNillable)
}


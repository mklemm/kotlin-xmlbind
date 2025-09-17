package net.codesup.tools.xkc.generator.poko

import com.sun.codemodel.*
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.FunctionDeclaration
import net.codesup.emit.declaration.KDocBuilder
import net.codesup.emit.declaration.ParameterDeclaration
import net.codesup.emit.use.KClassUse
import net.codesup.emit.use.TypeUse
import net.codesup.tools.xkc.model.ClassOutline


/**
 * The back-end may or may not generate the content interface
 * separately from the implementation class. If so, a method
 * needs to be declared on both the interface and the implementation class.
 *
 *
 * This class hides those details and allow callers to declare
 * methods just once.
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
abstract class MethodWriter protected constructor(context: ClassOutline) {
    val codeModel: SourceBuilder = context.parent.codeModel


    /**
     * Declares a method in both the interface and the implementation.
     *
     * @return
     * JMethod object that represents a newly declared method
     * on the implementation class.
     */
    abstract fun declareMethod(returnType: TypeUse, methodName: String): FunctionDeclaration

    fun <T:Any> declareMethod(returnType: Class<T>, methodName: String): FunctionDeclaration {
        return declareMethod(KClassUse<T>(returnType.kotlin), methodName)
    }

    /**
     * To generate javadoc for the previously declared method, use this method
     * to obtain a [JDocComment] object. This may return a value
     * different from declareMethod().javadoc().
     */
    abstract fun javadoc(): KDocBuilder

    /**
     * Adds a parameter to the previously declared method.
     *
     * @return
     * JVar object that represents a newly added parameter
     * on the implementation class.
     */
    abstract fun addParameter(type: TypeUse, name: String): ParameterDeclaration

    fun <T:Any> addParameter(type: Class<T>, name: String): ParameterDeclaration {
        return addParameter(KClassUse<T>(type.kotlin), name)
    }
}

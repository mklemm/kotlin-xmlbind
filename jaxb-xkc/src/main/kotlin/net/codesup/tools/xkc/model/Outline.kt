package net.codesup.tools.xkc.model

import com.sun.codemodel.*
import com.sun.tools.xjc.ErrorReceiver
import com.sun.tools.xjc.model.*
import net.codesup.emit.SourceBuilder
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.declaration.PackageDeclaration
import net.codesup.emit.use.ClassTypeUse
import net.codesup.tools.xkc.util.CodeModelClassFactory


/**
 * Root of the outline. Captures which code is generated for which model component.
 *
 *
 *
 * This object also provides access to various utilities, such as
 * error reporting etc, for the convenience of code that builds the outline.
 *
 * @author Kohsuke Kawaguchi
 */
interface Outline {
    /**
     * This outline is for this model.
     */
    val model: Model

    /**
     * Short for `getModel().codeModel`.
     */
    val codeModel: SourceBuilder

    /** Gets the object that wraps the generated field for a given [CPropertyInfo].  */
    fun getField(fu: CPropertyInfo): FieldOutline

    /**
     * Gets per-package context information.
     *
     * This method works for every visible package
     * (those packages which are supposed to be used by client applications.)
     *
     * @return
     * If this grammar doesn't produce anything in the specified
     * package, return null.
     */
    fun getPackageContext(_Package: PackageDeclaration): PackageOutline?

    /**
     * Returns all the [ClassOutline]s known to this object.
     */
    val classes: Collection<ClassOutline>

    /**
     * Obtains per-class context information.
     */
    fun getClazz(clazz: CClassInfo): ClassOutline

    /**
     * If the [CElementInfo] generates a class,
     * returns such a class. Otherwise return null.
     */
    fun getElement(ei: CElementInfo): ElementOutline
    fun getEnum(eli: CEnumLeafInfo): EnumOutline

    /**
     * Gets all the [EnumOutline]s.
     */
    val enums: Collection<EnumOutline>

    /** Gets all package-wise contexts at once.  */
    val allPackageContexts: Iterable<PackageOutline>

    /**
     * Gets a reference to
     * `new CodeModelClassFactory(getErrorHandler())`.
     */
    val classFactory: CodeModelClassFactory

    /**
     * Any error during the back-end proccessing should be
     * sent to this object.
     */
    val errorReceiver: ErrorReceiver

    fun getContainer(parent: CClassInfoParent, aspect: Aspect): DeclarationOwner<*>

    /**
     * Resolves a type reference to the actual (possibly generated) type.
     *
     * Short for `resolve(ref.getType(),aspect)`.
     */
    fun resolve(ref: CTypeRef, aspect: Aspect): net.codesup.emit.use.TypeUse

    /**
     * Copies the specified class into the user's package and returns
     * a reference to it.
     */
    fun addRuntime(clazz: Class<*>): ClassTypeUse
}

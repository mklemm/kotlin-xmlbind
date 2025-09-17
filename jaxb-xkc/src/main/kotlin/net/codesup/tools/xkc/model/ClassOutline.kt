package net.codesup.tools.xkc.model

import com.sun.tools.xjc.model.CClassInfo
import com.sun.tools.xjc.outline.FieldOutline
import com.sun.tools.xjc.outline.PackageOutline
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.use.ClassTypeUse


/**
 * Outline object that provides per-[CClassInfo] information
 * for filling in methods/fields for a bean.
 *
 * This interface is accessible from [Outline]
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
abstract class ClassOutline protected constructor(
    open val _package:PackageOutline,
    /**
     * This [ClassOutline] holds information about this [CClassInfo].
     */
    override val target: CClassInfo,
    /**
     * The exposed aspect of the a bean.
     *
     * implClass is always assignable to this type.
     *
     *
     * Usually this is the public content interface, but
     * it could be the same as the implClass.
     */
    val ref: ClassDeclaration,
    /**
     * The implementation class that shall be used for reference.
     *
     *
     * Usually this field holds the same value as the [.implClass] method,
     * but sometimes it holds the user-specified implementation class
     * when it is specified.
     *
     *
     * This is the type that needs to be used for generating fields.
     */
    val implRef: ClassTypeUse,
    override val implClass: ClassDeclaration
) :
    CustomizableOutline {
    /**
     * A [Outline] that encloses all the class outlines.
     */
    abstract val parent: net.codesup.tools.xkc.model.Outline

    /**
     * Gets all the [FieldOutline]s newly declared
     * in this class.
     */
    val declaredFields: Array<FieldOutline?>
        get() {
            val props = target.properties
            val fr = arrayOfNulls<FieldOutline>(props.size)
            for (i in fr.indices) fr[i] = parent.getField(props[i])
            return fr
        }

    /**
     * Returns the super class of this class, if it has the
     * super class, and it is also a JAXB-bound class.
     * Otherwise, null.
     */
    val superClass: ClassOutline
        get() = target.baseClass?.let { parent.classes[target.baseClass.fullName()] }

}

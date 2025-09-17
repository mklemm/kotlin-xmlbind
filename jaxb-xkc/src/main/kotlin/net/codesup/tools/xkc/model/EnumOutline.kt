package net.codesup.tools.xkc.model

import com.sun.tools.xjc.model.CEnumLeafInfo
import net.codesup.emit.declaration.ClassDeclaration


/**
 * Outline object that provides per-[CEnumLeafInfo] information
 * for filling in methods/fields for a bean.
 *
 * This object can be obtained from [Outline]
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
abstract class EnumOutline protected constructor(
    val _package: PackageOutline,
    /**
     * This [EnumOutline] holds information about this [CEnumLeafInfo].
     */
    override val target: CEnumLeafInfo,
    /**
     * The generated enum class.
     */
    override val implClass: ClassDeclaration
) :
    CustomizableOutline {
    /**
     * Constants.
     */
    val constants: List<EnumConstantOutline> = ArrayList()

    /**
     * A [Outline] that encloses all the class outlines.
     */

    abstract val parent: Outline
}

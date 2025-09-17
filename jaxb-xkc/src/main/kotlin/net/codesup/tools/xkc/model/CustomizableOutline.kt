package net.codesup.tools.xkc.model

import com.sun.tools.xjc.model.CCustomizable
import net.codesup.emit.declaration.ClassDeclaration


/**
 * This interface describes that outline class could be customized.
 * It provides the bound info from [CCustomizable] target. And
 * customization output - implementation class.
 *
 * @author yaroska
 * @since 2.2.12
 */
interface CustomizableOutline {
    /**
     * Provides bound information about customizable target.
     * @return customizable target
     */
    val target: CCustomizable

    /**
     * Provides customization output.
     * @return Implementation class
     */
    val implClass: ClassDeclaration
}

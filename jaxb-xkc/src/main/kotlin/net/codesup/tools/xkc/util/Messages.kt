package net.codesup.tools.xkc.util

import java.text.MessageFormat
import java.util.*


/**
 * Formats error messages.
 */
internal object Messages {
    /** Loads a string resource and formats it with specified arguments.  */
    fun format(property: String?, vararg args: Any?): String {
        val text =
            ResourceBundle.getBundle(Messages::class.java.getPackage().name + ".MessageBundle").getString(property)
        return MessageFormat.format(text, *args)
    }

    const val ERR_CLASSNAME_COLLISION = "CodeModelClassFactory.ClassNameCollision"
    const val ERR_CLASSNAME_COLLISION_SOURCE = "CodeModelClassFactory.ClassNameCollision.Source"
    const val ERR_INVALID_CLASSNAME = "ERR_INVALID_CLASSNAME"
    const val ERR_CASE_SENSITIVITY_COLLISION =  // 2 args
        "CodeModelClassFactory.CaseSensitivityCollision"
    const val ERR_CHAMELEON_SCHEMA_GONE_WILD =  // no argts
        "ERR_CHAMELEON_SCHEMA_GONE_WILD"
}

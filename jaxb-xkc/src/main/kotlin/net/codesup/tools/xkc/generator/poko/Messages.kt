package net.codesup.tools.xkc.generator.poko

import java.text.MessageFormat
import java.util.*


/**
 * Formats error messages.
 */
internal enum class Messages {
    // AnnotationParser
    METHOD_COLLISION,  // 3 args
    ERR_UNUSABLE_NAME,  // 2 args
    ERR_KEYNAME_COLLISION,  // 1 arg
    ERR_NAME_COLLISION,  // 1 arg
    ILLEGAL_CONSTRUCTOR_PARAM,  // 1 arg
    OBJECT_FACTORY_CONFLICT,  // 1 arg
    OBJECT_FACTORY_CONFLICT_RELATED;

    override fun toString(): String {
        return format()
    }

    fun format(vararg args: Any?): String {
        return MessageFormat.format(rb.getString(name), *args)
    }

    companion object {
        private val rb = ResourceBundle.getBundle(Messages::class.java.getPackage().name + ".MessageBundle")
    }
}

package net.codesup.tools.xkc

import org.xml.sax.ErrorHandler

/**
 * @author Mirko Klemm 2024-05-06
 *
 */
interface BuildEventListener {
    val errorHandler: ErrorHandler
    fun info(message: String)
    fun info(format: String, vararg args: Any) = info(format.format(*args))
}

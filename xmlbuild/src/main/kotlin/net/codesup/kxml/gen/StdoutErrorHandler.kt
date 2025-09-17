package net.codesup.kxml.gen

import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException

/**
 * @author Mirko Klemm 2025-09-11
 *
 */
class StdoutErrorHandler: ErrorHandler {
    override fun warning(exception: SAXParseException) {
        println("WARN  (${exception.lineNumber}:${exception.columnNumber}): ${exception.message})")
    }

    override fun error(exception: SAXParseException) {
        println("ERROR (${exception.lineNumber}:${exception.columnNumber}): ${exception.message})")
    }

    override fun fatalError(exception: SAXParseException) {
        println("FATAL (${exception.lineNumber}:${exception.columnNumber}): ${exception.message})")
    }
}

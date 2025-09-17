package net.codesup.tools.xkc

import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import java.io.PrintStream

/**
 * @author Mirko Klemm 2024-05-03
 *
 */
class StreamBuildEventListener(val outputStream: PrintStream, val errorStream: PrintStream, val threshold: Int): BuildEventListener {

    override val errorHandler: ErrorHandler = object:ErrorHandler {
            override fun warning(exception: SAXParseException) {
                if (threshold > 2) {
                    outputStream.println(exception)
                }
            }

            override fun error(exception: SAXParseException) {
                if (threshold > 1) {
                    errorStream.println(exception)
                }
            }

            override fun fatalError(exception: SAXParseException) {
                if (threshold > 0) {
                    errorStream.println(exception)
                }
            }
        }

    override fun info(message: String) {
        outputStream.println(message)
    }
}

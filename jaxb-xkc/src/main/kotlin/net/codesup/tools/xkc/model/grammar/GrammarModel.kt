package net.codesup.tools.xkc.model.grammar

import com.sun.xml.xsom.parser.XMLParser
import net.codesup.tools.xkc.BuildEventListener
import org.xml.sax.ContentHandler
import org.xml.sax.EntityResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import java.net.URI

/**
 * @author Mirko Klemm 2024-05-03
 *
 */
class GrammarModel {
    companion object {
        fun loadGrammars(grammarUris: List<URI>, errorHandler: BuildEventListener): GrammarModel {
            grammarUris.forEach { g ->

            }
        }
    }
}

class SpeculativeParser:XMLParser {
    override fun parse(inputSource: InputSource, contentHandler: ContentHandler, errorHandler: ErrorHandler, entityResolver: EntityResolver?) {

    }

}

package net.codesup.tools.xkc

import org.xml.sax.EntityResolver
import org.xml.sax.ErrorHandler
import org.xml.sax.InputSource
import java.nio.file.FileSystem

/**
 * @author Mirko Klemm 2024-05-03
 *
 */
class Options(val grammars:List<InputSource>,
              val bindings:List<InputSource>,
              val output: FileSystem,
              val useEpisode: InputSource = InputSource("sun-jaxb.episode"),
              val writeEpisode: Boolean = true,
              val entityResolver: EntityResolver,
              val errorHandler: ErrorHandler = StreamErrorHandler(System.out, System.err, 1),
              val plugins: List<Plugin> = listOf()
) {

}

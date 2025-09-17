package ee.jakarta.xml.ns.persistence.orm

import javax.xml.stream.XMLStreamWriter

class InheritanceBuilder(val xmlWriter: XMLStreamWriter) {

	fun strategy(strategy: InheritanceType) {
		xmlWriter.writeAttribute("strategy", strategy.value)
	}

}

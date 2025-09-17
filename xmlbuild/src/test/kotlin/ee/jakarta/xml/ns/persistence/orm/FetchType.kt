package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

enum class FetchType(val value: String) {

	LAZY("LAZY"),
	EAGER("EAGER"),
}

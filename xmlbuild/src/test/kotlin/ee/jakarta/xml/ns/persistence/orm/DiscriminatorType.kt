package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

enum class DiscriminatorType(val value: String) {

	STRING("STRING"),
	CHAR("CHAR"),
	INTEGER("INTEGER"),
}

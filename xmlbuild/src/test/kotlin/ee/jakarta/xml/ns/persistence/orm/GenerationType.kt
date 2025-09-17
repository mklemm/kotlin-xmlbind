package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

enum class GenerationType(val value: String) {

	TABLE("TABLE"),
	SEQUENCE("SEQUENCE"),
	IDENTITY("IDENTITY"),
	UUID("UUID"),
	AUTO("AUTO"),
}

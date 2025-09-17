package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

enum class InheritanceType(val value: String) {

	SINGLETABLE("SINGLE_TABLE"),
	JOINED("JOINED"),
	TABLEPERCLASS("TABLE_PER_CLASS"),
}

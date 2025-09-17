package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

enum class ParameterMode(val value: String) {

	IN("IN"),
	INOUT("INOUT"),
	OUT("OUT"),
	REFCURSOR("REF_CURSOR"),
}

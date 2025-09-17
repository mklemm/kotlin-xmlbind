package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

enum class ConstraintMode(val value: String) {

	CONSTRAINT("CONSTRAINT"),
	NOCONSTRAINT("NO_CONSTRAINT"),
	PROVIDERDEFAULT("PROVIDER_DEFAULT"),
}

package net.codesup.jaxb

import jakarta.xml.bind.annotation.adapters.XmlAdapter
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.util.*


val whitespaceRegex = Regex("\\s+")
/**
 * Accepts the ISO 8601 Local DateTime and OffsetDateTime formats.
 * If source contains a time zone offset, the datetime
 * representation will be converted into the local time zone
 */
class LocalDateTimeXmlAdapter : XmlAdapter<String?, LocalDateTime?>() {
	override fun unmarshal(value: String?):LocalDateTime? = value?.trim()?.replace(whitespaceRegex,"T")?.let { s ->
		if (s.length > 19) {
			DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(s, ::queryOffsetDateTime)
		} else {
			DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(s, LocalDateTime::from)
		}
	}
	override fun marshal(value: LocalDateTime?) = value?.let { DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(it) }
	private fun queryOffsetDateTime(it: TemporalAccessor): LocalDateTime = OffsetDateTime.from(it).atZoneSameInstant(ZoneOffset.systemDefault()).toLocalDateTime()
}

/**
 * Accepts the ISO 8601 Local Date and OffsetDate formats.
 * If source contains a time zone offset, the datetime
 * representation will be converted into the local time zone
 */
class LocalDateXmlAdapter : XmlAdapter<String?, LocalDate?>() {
	override fun unmarshal(value: String?):LocalDate? = value?.let { s ->
		if (s.length > 10) {
			DateTimeFormatter.ISO_OFFSET_DATE.parse(s, ::queryOffsetDateAsLocal)
		} else {
			DateTimeFormatter.ISO_LOCAL_DATE.parse(s, LocalDate::from)
		}
	}
	override fun marshal(value: LocalDate?) = value?.let { DateTimeFormatter.ISO_LOCAL_DATE.format(it) }
	private fun queryOffsetDateAsLocal(parsed: TemporalAccessor): LocalDate = queryOffsetDate(parsed).atZoneSameInstant(ZoneOffset.systemDefault()).toLocalDate()
}

/**
 * Accepts the ISO 8601 Local DateTime and OffsetDateTime formats.
 * If source contains no time zone offset, the datetime
 * representation will be interpreted as being in the current system
 * local time zone.
 */
class OffsetDateTimeXmlAdapter : XmlAdapter<String?, OffsetDateTime?>() {
	override fun unmarshal(value: String?): OffsetDateTime? = value?.trim()?.replace(whitespaceRegex,"T")?.let {
		if (value.length > 19) {
			DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(value, OffsetDateTime::from)
		} else {
			DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(value, LocalDateTime::from).let { it.atOffset(ZoneOffset.systemDefault().rules.getOffset(it)) }
		}
	}

	override fun marshal(value: OffsetDateTime?) = value?.let { DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(it) }
}

/**
 * Accepts the ISO 8601 Local Date and OffsetDate formats.
 * If source contains no time zone offset, the datetime
 * representation will be interpreted as being in the current system
 * local time zone.
 */
class OffsetDateXmlAdapter : XmlAdapter<String?, OffsetDateTime?>() {
	override fun unmarshal(value: String?): OffsetDateTime? = value?.let { s ->
		if (s.length > 10) {
			DateTimeFormatter.ISO_OFFSET_DATE.parse(s, ::queryOffsetDate)
		} else {
			DateTimeFormatter.ISO_LOCAL_DATE.parse(s, LocalDate::from)
				.let { ld ->
					ld.atTime(LocalTime.of(0, 0, 0, 0))
						.let { ldt -> ldt.atOffset(ZoneOffset.systemDefault().rules.getOffset(ldt)) }
				}
		}
	}

	override fun marshal(value: OffsetDateTime?) = value?.let { DateTimeFormatter.ISO_OFFSET_DATE.format(it) }
}

/**
 * Accepts only the ISO 8601 Local DateTime format.
 * If source contains a time zone offset, a parsing error will occur
 */
class StrictLocalDateTimeXmlAdapter : XmlAdapter<String?, LocalDateTime?>() {
	override fun unmarshal(value: String?):LocalDateTime? = value?.let { s -> DateTimeFormatter.ISO_LOCAL_DATE_TIME.parse(s, LocalDateTime::from) }
	override fun marshal(value: LocalDateTime?) = value?.let { DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(it) }
}

/**
 * Accepts only the ISO 8601 Local Date format.
 * If source contains a time zone offset, a parsing error will occur
 */
class StrictLocalDateXmlAdapter : XmlAdapter<String?, LocalDate?>() {
	override fun unmarshal(value: String?):LocalDate? = value?.let { s -> DateTimeFormatter.ISO_LOCAL_DATE.parse(s, LocalDate::from) }
	override fun marshal(value: LocalDate?) = value?.let { DateTimeFormatter.ISO_LOCAL_DATE.format(it) }
}

/**
 * Accepts only the ISO 8601 OffsetDateTime format.
 * If source contains no time zone offset, a parsing error will occur
 */
class StrictOffsetDateTimeXmlAdapter : XmlAdapter<String?, OffsetDateTime?>() {
	override fun unmarshal(value: String?): OffsetDateTime? = value?.let {DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(value, OffsetDateTime::from) }
	override fun marshal(value: OffsetDateTime?) = value?.let { DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(it) }
}

/**
 * Accepts only the ISO 8601 OffsetDate format.
 * If source contains no time zone offset, a parsing error will occur
 */
class StrictOffsetDateXmlAdapter : XmlAdapter<String?, OffsetDateTime?>() {
	override fun unmarshal(value: String?): OffsetDateTime? = value?.let { s -> DateTimeFormatter.ISO_OFFSET_DATE.parse(s, ::queryOffsetDate) }
	override fun marshal(value: OffsetDateTime?) = value?.let { DateTimeFormatter.ISO_OFFSET_DATE.format(it) }
}

private fun queryOffsetDate(it: TemporalAccessor): OffsetDateTime
	= LocalDate.from(it)
	.atTime(OffsetTime.of(0, 0, 0, 0, ZoneOffset.ofTotalSeconds(it.get(ChronoField.OFFSET_SECONDS))))

class UuidXmlAdapter:XmlAdapter<String?, UUID>() {
    override fun unmarshal(v: String?) = v?.let { UUID.fromString(it) }

    override fun marshal(v: UUID?) = v?.toString()

}

val offsetDateTimeXmlAdapter = OffsetDateTimeXmlAdapter()
val offsetDateXmlAdapter = OffsetDateXmlAdapter()

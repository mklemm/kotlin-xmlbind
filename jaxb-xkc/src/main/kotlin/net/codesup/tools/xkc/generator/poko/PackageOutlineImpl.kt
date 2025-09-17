package net.codesup.tools.xkc.generator.poko

import com.sun.tools.xjc.generator.annotation.spec.XmlSchemaWriter
import com.sun.tools.xjc.model.*
import com.sun.tools.xjc.outline.Aspect
import jakarta.xml.bind.annotation.XmlNsForm
import net.codesup.tools.xkc.model.ClassOutline
import net.codesup.tools.xkc.model.PackageOutline
import net.codesup.emit.declaration.PackageDeclaration
import java.util.*
import javax.xml.namespace.QName


/**
 * [PackageOutline] enhanced with schema2java specific
 * information.
 *
 * @author
 * Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com), Martin Grebac (martin.grebac@oracle.com)
 */
class PackageOutlineImpl protected constructor(
    outline: BeanGenerator,
    private val _model: Model,
    override val _package: PackageDeclaration
) :
    PackageOutline {
    override var objectFactoryGenerator: ObjectFactoryGenerator = null

    /*package*/
    override val classes: MutableSet<ClassOutline> = HashSet()
    private val classesView = Collections.unmodifiableSet(classes)
    override var mostUsedNamespaceURI: String? = null
    override var elementFormDefault: XmlNsForm = XmlNsForm.QUALIFIED
    override var attributeFormDefault: XmlNsForm = XmlNsForm.UNQUALIFIED


    /**
     * Compute the most common namespace URI in this package
     * (to put into [XmlSchema.namespace] and what value
     * we should put into [XmlSchema.elementFormDefault].
     *
     * This method is called after [.classes] field is filled up.
     */
    fun calcDefaultValues() {
        // short-circuit if xjc was told not to generate package level annotations in
        // package-info.java
        if (!_model.isPackageLevelAnnotations) {
            mostUsedNamespaceURI = ""
            elementFormDefault = XmlNsForm.UNQUALIFIED
            return
        }

        // used to visit properties
        val propVisitor: CPropertyVisitor<Void> = object : CPropertyVisitor<Void?> {
            override fun onElement(p: CElementPropertyInfo): Void? {
                for (tr in p.types) {
                    countURI(propUriCountMap, tr.tagName)
                }
                return null
            }

            override fun onReference(p: CReferencePropertyInfo): Void? {
                for (e in p.elements) {
                    countURI(propUriCountMap, e.elementName)
                }
                return null
            }

            override fun onAttribute(p: CAttributePropertyInfo): Void? {
                return null
            }

            override fun onValue(p: CValuePropertyInfo): Void? {
                return null
            }
        }
        for (co in classes) {
            val ci = co.target
            countURI(uriCountMap, ci.typeName)
            countURI(uriCountMap, ci.elementName)
            for (p in ci.properties) p.accept(propVisitor)
        }
        mostUsedNamespaceURI = getMostUsedURI(uriCountMap)
        elementFormDefault = formDefault
        attributeFormDefault = XmlNsForm.UNQUALIFIED
        try {
            val modelValue = _model.getAttributeFormDefault(mostUsedNamespaceURI)
            attributeFormDefault = modelValue
        } catch (e: Exception) {
            // ignore and accept default
        }

        // generate package-info.java
        // we won't get this far if the user specified -npa
        if (mostUsedNamespaceURI != "" || elementFormDefault == XmlNsForm.QUALIFIED || attributeFormDefault == XmlNsForm.QUALIFIED) {
            val w = _model.strategy.getPackage(_package, Aspect.IMPLEMENTATION).annotate2(
                XmlSchemaWriter::class.java
            )
            if (mostUsedNamespaceURI != "") w.namespace(mostUsedNamespaceURI)
            if (elementFormDefault == XmlNsForm.QUALIFIED) w.elementFormDefault(elementFormDefault)
            if (attributeFormDefault == XmlNsForm.QUALIFIED) w.attributeFormDefault(attributeFormDefault)
        }
    }

    // Map to keep track of how often each type or element uri is used in this package
    // mostly used to calculate mostUsedNamespaceURI
    private val uriCountMap = HashMap<String, Int>()

    // Map to keep track of how often each property uri is used in this package
    // used to calculate elementFormDefault
    private val propUriCountMap = HashMap<String, Int>()

    init {
        objectFactoryGenerator =
            when (_model.strategy) {
                ImplStructureStrategy.BEAN_ONLY -> PublicObjectFactoryGenerator(
                    outline,
                    _model,
                    _package
                )
                ImplStructureStrategy.INTF_AND_IMPL -> DualObjectFactoryGenerator(
                    outline,
                    _model,
                    _package
                )
                else -> throw IllegalStateException()
            }
    }

    /**
     * pull the uri out of the specified QName and keep track of it in the
     * specified hash map
     *
     * @param qname
     */
    private fun countURI(map: HashMap<String, Int>, qname: QName?) {
        if (qname == null) return
        val uri = qname.namespaceURI
        if (map.containsKey(uri)) {
            map[uri] = map[uri]!! + 1
        } else {
            map[uri] = 1
        }
    }

    /**
     * Iterate through the hash map looking for the namespace used
     * most frequently.  Ties are arbitrarily broken by the order
     * in which the map keys are iterated over.
     *
     *
     *
     * Because JAX-WS often reassigns the "" namespace URI,
     * and when that happens it unintentionally also renames (normally
     * unqualified) local elements, prefer non-"" URI when there's a tie.
     */
    private fun getMostUsedURI(map: HashMap<String, Int>): String {
        var mostPopular: String? = null
        var count = 0
        for ((uri, uriCount) in map) {
            if (mostPopular == null) {
                mostPopular = uri
                count = uriCount
            } else {
                if (uriCount > count || uriCount == count && mostPopular == "") {
                    mostPopular = uri
                    count = uriCount
                }
            }
        }
        return if (mostPopular == null) "" else mostPopular
    }

    /**
     * Calculate the element form defaulting.
     *
     * Compare the most frequently used property URI to the most frequently used
     * element/type URI.  If they match, then return QUALIFIED
     */
    private val formDefault: XmlNsForm
        private get() = if (getMostUsedURI(propUriCountMap) == "") XmlNsForm.UNQUALIFIED else XmlNsForm.QUALIFIED
}

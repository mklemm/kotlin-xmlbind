package net.codesup.tools.xkc.generator.poko

import com.sun.codemodel.*
import com.sun.tools.xjc.model.CElementInfo
import jakarta.xml.bind.JAXBElement
import net.codesup.emit.ConstructorInv
import net.codesup.emit.SourceBuilder
import net.codesup.emit.use.KClassUse
import net.codesup.tools.xkc.model.Aspect
import net.codesup.tools.xkc.model.ElementOutline
import javax.xml.namespace.QName


/**
 * [ElementOutline] implementation.
 *
 * @author Kohsuke Kawaguchi
 */
class ElementOutlineImpl(private val parent: BeanGenerator, ei: CElementInfo) :
    ElementOutline(
        ei,
        parent.classFactory.createClass(
            parent.getContainer(ei.parent, Aspect.EXPOSED), ei.shortName(), ei.locator
        )
    ) {
    override fun parent(): BeanGenerator {
        return parent
    }

    /*package*/
    init {
        parent.elements[ei] = this
        val cm = parent.codeModel
        implClass._extends(
            cm.ref(JAXBElement::class.java).narrow(
                target.contentInMemoryType.toType(parent, Aspect.EXPOSED).boxify()
            )
        )
        if (ei.hasClass()) {
            val implType = ei.contentInMemoryType.toType(parent, Aspect.IMPLEMENTATION)
            val declaredType: JExpression =
                JExpr.cast(cm.ref(Class::class.java), implType.boxify().dotclass()) // why do we have to cast?
            var scope: JClass? = null
            if (ei.scope != null) scope = parent.getClazz(ei.scope).implRef
            val scopeClass = if (scope == null) JExpr._null() else scope.dotclass()
            val valField = implClass.field(
                JMod.PROTECTED or JMod.FINAL or JMod.STATIC,
                QName::class.java, "NAME", createQName(cm, ei.elementName)
            )

            // take this opportunity to generate a constructor in the element class
            val cons = implClass.constructor(JMod.PUBLIC)
            cons.body().invoke("super")
                .arg(valField)
                .arg(declaredType)
                .arg(scopeClass)
                .arg(cons.param(implType, "value"))

            // generate no-arg constructor in the element class (bug #391; section 5.6.2 in JAXB spec 2.1)
            val noArgCons = implClass.constructor(JMod.PUBLIC)
            noArgCons.body().invoke("super")
                .arg(valField)
                .arg(declaredType)
                .arg(scopeClass)
                .arg(JExpr._null())
        }
    }

    /**
     * Generates an expression that evaluates to "new QName(...)"
     */
    private fun createQName(codeModel: SourceBuilder, name: QName): ConstructorInv = KClassUse(QName::class)._new {
            arg(context.str(name.namespaceURI))
            arg(context.str(name.localPart))
        }


}

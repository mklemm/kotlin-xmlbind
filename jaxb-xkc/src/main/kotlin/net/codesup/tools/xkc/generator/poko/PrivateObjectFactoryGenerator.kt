package net.codesup.tools.xkc.generator.poko

import com.sun.tools.xjc.generator.bean.BeanGenerator
import com.sun.tools.xjc.generator.bean.ClassOutlineImpl
import com.sun.tools.xjc.model.CElementInfo
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Aspect
import com.sun.tools.xjc.runtime.JAXBContextFactory
import net.codesup.jaxb.types.toKotlin
import net.codesup.emit.declaration.PackageDeclaration


/**
 * Generates private ObjectFactory.
 *
 *
 *
 * This class also puts a copy of [JAXBContextFactory]
 * to the impl package.
 *
 * @author Kohsuke Kawaguchi
 */
internal class PrivateObjectFactoryGenerator(outline: BeanGenerator, model: Model, targetPackage: PackageDeclaration) :
    ObjectFactoryGeneratorImpl(outline, model, targetPackage._package("impl")) {
    init {
        val implPkg = targetPackage._package("impl")

//        // put JAXBContextFactory into the impl package
//        val factory = outline.generateStaticClass(JAXBContextFactory::class.java, implPkg)
//
//        // and then put jaxb.properties to point to it
//        val jaxbProperties = JPropertyFile("jaxb.properties")
//        targetPackage.addResourceFile(jaxbProperties)
//        jaxbProperties.add(
//            JAXBContext.JAXB_CONTEXT_FACTORY,
//            factory.fullName()
//        )
    }

    override fun populate(ei: CElementInfo) {
        populate(ei, Aspect.IMPLEMENTATION, Aspect.IMPLEMENTATION)
    }

    override fun populate(cc: ClassOutlineImpl) {
        populate(cc, cc.implRef.toKotlin())
    }
}

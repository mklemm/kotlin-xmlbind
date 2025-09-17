package net.codesup.tools.xkc.generator.poko

import com.sun.tools.xjc.model.CElementInfo
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Aspect
import net.codesup.emit.declaration.PackageDeclaration
import net.codesup.tools.xkc.model.ClassOutline


/**
 * Generates public ObjectFactory.
 *
 * @author Kohsuke Kawaguchi
 */
internal class PublicObjectFactoryGenerator(outline: BeanGenerator, model: Model, targetPackage: PackageDeclaration) :
    ObjectFactoryGeneratorImpl(outline, model, targetPackage) {
    override fun populate(ei: CElementInfo) {
        populate(ei, Aspect.IMPLEMENTATION, Aspect.EXPOSED)
    }

    override fun populate(cc: ClassOutline) {
        populate(cc, cc.ref)
    }
}

package net.codesup.tools.xkc.generator.poko

import com.sun.tools.xjc.model.CElementInfo
import com.sun.tools.xjc.model.Model
import net.codesup.tools.xkc.model.ClassOutline
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.declaration.PackageDeclaration


/**
 * [ObjectFactoryGenerator] used when we generate
 * interfaces and implementations in separate packages.
 *
 *
 *
 * [.publicOFG] and [.privateOFG] gives you access to
 * `ObjectFactory`s in both packages, if you need to.
 *
 * @author Kohsuke Kawaguchi
 */
class DualObjectFactoryGenerator internal constructor(
    outline: BeanGenerator,
    model: Model,
    targetPackage: PackageDeclaration
) :
    ObjectFactoryGenerator() {
    val publicOFG: ObjectFactoryGenerator
    val privateOFG: ObjectFactoryGenerator

    init {
        publicOFG = PublicObjectFactoryGenerator(outline, model, targetPackage)
        privateOFG = PrivateObjectFactoryGenerator(outline, model, targetPackage)

        // put the marker so that we can detect missing jaxb.properties
        publicOFG.objectFactory._companion()._val("_useJAXBProperties") {
            type(Void::class)
            init {
                top = _null()
            }
        }
    }

    override fun populate(ei: CElementInfo) {
        publicOFG.populate(ei)
        privateOFG.populate(ei)
    }

    override fun populate(cc: ClassOutline) {
        publicOFG.populate(cc)
        privateOFG.populate(cc)
    }

    /**
     * Returns the private version (which is what gets used at runtime.)
     */
    override val objectFactory: ClassDeclaration = privateOFG.objectFactory

}

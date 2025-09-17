package net.codesup.tools.xkc.generator.poko

import com.sun.tools.xjc.model.CClassInfo
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.use.ClassTypeUse
import net.codesup.tools.xkc.model.ClassOutline


/**
 * [ClassOutline] enhanced with schema2java specific
 * information.
 *
 * @author
 * Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
class ClassOutlineImpl internal constructor(
    private val _parent: BeanGenerator,
    _target: CClassInfo?, exposedClass: ClassDeclaration, _implClass: ClassDeclaration, _implRef: ClassTypeUse
) : ClassOutline(_target, exposedClass, _implRef, _implClass) {
    fun createMethodWriter(): MethodWriter {
        return _parent.model.strategy.createMethodWriter(this)
    }

    /**
     * Gets [._package] as [PackageOutlineImpl],
     * since it's guaranteed to be of that type.
     */
    override fun _package(): PackageOutlineImpl {
        return super._package() as PackageOutlineImpl
    }

    init {
        _package().classes.add(this)
    }

    override fun parent(): BeanGenerator {
        return _parent
    }
}

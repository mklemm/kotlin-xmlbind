package net.codesup.tools.xkc.generator.poko

import com.sun.tools.xjc.model.CElementInfo
import net.codesup.tools.xkc.model.ClassOutline
import net.codesup.emit.declaration.ClassDeclaration


/**
 * Generates `ObjectFactory` then wraps it and provides
 * access to it.
 *
 *
 *
 * The ObjectFactory contains
 * factory methods for each schema derived content class
 *
 * @author
 * Ryan Shoemaker
 */
abstract class ObjectFactoryGenerator {
    /**
     * Adds code for the given [CElementInfo] to ObjectFactory.
     */
    abstract fun populate(ei: CElementInfo)

    /**
     * Adds code that is relevant to a given [ClassOutlineImpl] to
     * ObjectFactory.
     */
    abstract fun populate(cc: ClassOutline)

    /**
     * Returns a reference to the generated (public) ObjectFactory
     */
    abstract val objectFactory: ClassDeclaration
}

package net.codesup.tools.xkc.util

import com.sun.tools.xjc.ErrorReceiver
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.declaration.ClassifierKind
import net.codesup.emit.declaration.DeclarationOwner
import org.xml.sax.Locator


/**
 * Create new [JDefinedClass] and report class collision errors,
 * if necessary.
 *
 * This is just a helper class that simplifies the class name collision
 * detection. This object maintains no state, so it is OK to use
 * multiple instances of this.
 *
 * @author
 * Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
class CodeModelClassFactory(
    /** errors are reported to this object.  */
    private val errorReceiver: ErrorReceiver
) {
    /** unique id generator.  */
    private var ticketMaster = 0
    fun createClass(parent: DeclarationOwner<ClassDeclaration>, name: String, source: Locator): ClassDeclaration {
        return createClass(parent, name, source)
    }

    fun createInterface(parent: DeclarationOwner<ClassDeclaration>, name: String, source: Locator): ClassDeclaration {
        return createClass(parent, name, source, ClassifierKind.INTERFACE)
    }

    fun createClass(
        parent: DeclarationOwner<ClassDeclaration>, name: String, source: Locator, kind: ClassifierKind
    ): ClassDeclaration {
        return createClass(parent, name, source, kind)
    }

    @JvmOverloads
    fun createClass(
        parent: DeclarationOwner<ClassDeclaration>,
        mod: Int,
        name: String,
        source: Locator,
        kind: ClassifierKind = ClassifierKind.CLASS
    ): ClassDeclaration = parent._classifier(name, kind) {

        // use the metadata field to store the source location,
        // so that we can report class name collision errors.
        metadata = source

    }


    /**
     * Create a dummy class to recover from an error.
     *
     * We won't generate the code, so the client will never see this class
     * getting generated.
     */
    private fun createDummyClass(parent: DeclarationOwner<ClassDeclaration>) =
        parent._class("$$\$garbage$$$" + ticketMaster++)

}

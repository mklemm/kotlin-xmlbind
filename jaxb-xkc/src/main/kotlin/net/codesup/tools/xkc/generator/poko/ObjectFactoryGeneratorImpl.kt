package net.codesup.tools.xkc.generator.poko

import com.sun.codemodel.JCodeModel
import com.sun.codemodel.JExpr
import com.sun.tools.xjc.model.CElementInfo
import com.sun.tools.xjc.model.CPropertyInfo
import com.sun.tools.xjc.model.Constructor
import com.sun.tools.xjc.model.Model
import com.sun.tools.xjc.outline.Aspect
import jakarta.xml.bind.annotation.*
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter
import net.codesup.jaxb.types.toKotlin
import net.codesup.tools.xkc.model.ClassOutline
import net.codesup.emit.Annotatable
import net.codesup.emit.Assign
import net.codesup.emit.Expression
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.declaration.PackageDeclaration
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.KClassUse
import org.glassfish.jaxb.core.v2.model.core.ID
import org.glassfish.jaxb.core.v2.runtime.SwaRefAdapterMarker
import javax.xml.namespace.QName


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
internal abstract class ObjectFactoryGeneratorImpl(
    private val outline: BeanGenerator,
    model: Model,
    targetPackage: PackageDeclaration
) :
    ObjectFactoryGenerator() {
    private val codeModel: JCodeModel = model.codeModel

    /**
     * Ref to [Class].
     */
    private val classRef: KClassUse<Class<*>> = KClassUse(Class::class)

    /**
     * Reference to the generated ObjectFactory class.
     */
    final override val objectFactory: ClassDeclaration

    /** map of qname to the QName constant field.  */
    private val qnameMap = HashMap<QName, Expression>()

    /**
     * Names of the element factory methods that are created.
     * Used to detect collisions.
     *
     * The value is used for reporting error locations.
     */
    private val elementFactoryNames: MutableMap<String, CElementInfo> = HashMap()

    /**
     * Names of the value factory methods that are created.
     * Used to detect collisions.
     *
     * The value is used for reporting error locations.
     */
    private val valueFactoryNames: MutableMap<String, ClassOutlineImpl> = HashMap()

    init {

        // create the ObjectFactory class skeleton
        objectFactory = targetPackage._class("ObjectFactory") {
            annotate(KClassUse(XmlRegistry::class)) {}

            // generate the default constructor
            //
            // m1 result:
            //        public ObjectFactory() {}
            val m1 = _constructor {
                doc(
                    "Create a new ObjectFactory that can be used to " +
                            "create new instances of schema derived classes " +
                            "for package: " + targetPackage.name
                )
            }


            // add some class javadoc
            doc(
                "This object contains factory methods for each \n" +
                        "Java content interface and Java element interface \n" +
                        "generated in the " + targetPackage.name + " package. \n" +
                        "<p>An ObjectFactory allows you to programatically \n" +
                        "construct new instances of the Java representation \n" +
                        "for XML content. The Java representation of XML \n" +
                        "content can consist of schema derived interfaces \n" +
                        "and classes representing the binding of schema \n" +
                        "type definitions, element declarations and model \n" +
                        "groups.  Factory methods for each of these are \n" +
                        "provided in this class."
            )
        }
    }

    /**
     * Adds code for the given [CElementInfo] to ObjectFactory.
     */
    protected fun populate(ei: CElementInfo, impl: Aspect, exposed: Aspect) {
        val exposedElementType = ei.toType(outline, exposed).toKotlin()
        val exposedType = ei.contentInMemoryType.toType(outline, exposed).toKotlin()
        val implType = ei.contentInMemoryType.toType(outline, impl).toKotlin()
        val namespaceURI = ei.elementName.namespaceURI
        val localPart = ei.elementName.localPart
        val scope = if (ei.scope != null) outline.getClazz(ei.scope).implClass.toKotlin() else null

        if (ei.isAbstract) {
            // TODO: see the "Abstract elements and mighty IXmlElement" e-mail
            // that I sent to jaxb-tech
            org.glassfish.jaxb.core.v2.TODO.checkSpec()
        }
        // collision check
        val existing: CElementInfo? = elementFactoryNames.put(ei.getSqueezedName(), ei)
        if (existing != null) {
            outline.getErrorReceiver().error(
                existing.getLocator(),
                Messages.OBJECT_FACTORY_CONFLICT.format(ei.getSqueezedName())
            )
            outline.getErrorReceiver().error(
                ei.getLocator(),
                Messages.OBJECT_FACTORY_CONFLICT_RELATED.format()
            )
            return
        }

        // no arg constructor
        // [RESULT] if the element doesn't have its own class, something like:
        //
        //        @XmlElementMapping(uri = "", name = "foo")
        //        public JAXBElement<Foo> createFoo( Foo value ) {
        //            return new JAXBElement<Foo>(
        //                new QName("","foo"),(Class)FooImpl.class,scope,(FooImpl)value);
        //        }
        //        NOTE: when we generate value classes Foo==FooImpl
        //
        // [RESULT] otherwise
        //
        //        @XmlElementMapping(uri = "", name = "foo")
        //        public Foo createFoo( FooType value ) {
        //            return new Foo((FooTypeImpl)value);
        //        }
        //        NOTE: when we generate value classes FooType==FooTypeImpl
        //
        // to deal with
        //  new JAXBElement<List<String>>( ..., List.class, ... );
        // we sometimes have to produce (Class)List.class instead of just List.class
        val m = objectFactory._fun("create" + ei.squeezedName) {
            returnType(exposedElementType)
            val valueParam = param("value") {
                type(exposedType) {}
            }
            val declaredType = if (implType.isParameterized || exposedType != implType)
                implType.dotClass().cast(classRef)
            else
                implType.dotClass()
            val scopeClass = if (scope == null) valueParam.use()._null() else scope.dotClass()

            // build up the return expression
            block {
                top = exposedElementType._new {
                    if (!ei.hasClass()) {
                        arg(getQNameInvocation(ei))
                        arg(declaredType)
                        arg(scopeClass)
                    }
                    if (implType === exposedType) arg(valueParam) else arg(valueParam.use().variable.cast(implType))
                }
            }
//            m.javadoc()
//                .append("Create an instance of ")
//                .append(exposedElementType)
//            m.javadoc().addParam(`$value`)
//                .append("Java instance representing xml element's value.")
//            m.javadoc().addReturn()
//                .append("the new instance of ")
//                .append(exposedElementType)
            annotate(XmlElementDecl::class) {
                args {
                    arg("namespace", str(namespaceURI))
                    arg("name", str(localPart))

                    if (scope != null) arg("scope", scope)
                    if (ei.substitutionHead != null) {
                        val n = ei.substitutionHead.elementName
                        arg("substitutionHeadNamespace", str(n.namespaceURI))
                        arg("substitutionHeadName", str(n.localPart))
                    }
                    if (ei.defaultValue != null) arg("defaultValue", str(ei.defaultValue))
                }
            }
            if (ei.property.inlineBinaryData()) annotate(XmlInlineBinaryData::class)
        }
        // if the element is adapter, put that annotation on the factory method
        generateAdapterIfNecessary(ei.property, m)
    }

    fun generateAdapterIfNecessary(prop: CPropertyInfo, field: Annotatable) {
        val adapter = prop.adapter
        if (adapter != null) {
            if (adapter.adapterIfKnown == SwaRefAdapterMarker::class.java) {
                field.annotate(XmlAttachmentRef::class)
            } else {
                // [RESULT]
                // @XmlJavaTypeAdapter( Foo.class )
                field.annotate(XmlJavaTypeAdapter::class) {
                    args {
                        arg("value", adapter.adapterType.toType(outline, Aspect.EXPOSED).toKotlin())
                    }
                }
            }
        }
        when (prop.id()) {
            ID.ID -> field.annotate(XmlID::class)
            ID.IDREF -> field.annotate(XmlIDREF::class)
        }
        if (prop.expectedMimeType != null) {
            field.annotate(XmlMimeType::class) {
                args {
                    arg(str(prop.expectedMimeType.toString()))
                }
            }
        }
    }

    /**
     * return a JFieldVar that represents the QName field for the given information.
     *
     * if it doesn't exist, create a static field in the class and store a new JFieldVar.
     */
    private fun getQNameInvocation(ei: CElementInfo): Expression {
        val name = ei.elementName
        if (qnameMap.containsKey(name)) {
            return qnameMap[name]!!
        }
        if (qnameMap.size > 1024) // stop gap measure to avoid 'code too large' error in javac.
            return createQName(name)

        // [RESULT]
        // private static final QName _XYZ_NAME = new QName("uri", "local");
        val qnameField = objectFactory._companion()._val('_'.toString() + ei.squeezedName + "_QNAME") {
            type(KClassUse(QName::class))
            init(createQName(name))
            annotate(JvmStatic::class)
        }
        qnameMap[name] = qnameField.use().variable
        return qnameField
    }

    /**
     * Generates an expression that evaluates to "new QName(...)"
     */
    private fun createQName(name: QName): Assign = Assign(KClassUse(QName::class)._new {
        arg(context.str(name.namespaceURI))
        arg(context.str(name.localPart))
    })


    protected fun populate(cc: ClassOutline, sigType: ClassTypeUse) {
        // add static factory method for this class to JAXBContext.
        //
        // generate methods like:
        //     public static final SIGTYPE createFoo() {
        //         return new FooImpl();
        //     }
        if (!cc.target.isAbstract) {
            val m = objectFactory._fun("create" + cc.target.squeezedName) {
                returnType(sigType)
                assign { cc.implClass.use()._new {} }
            }
        }

        // add static factory methods for all the other constructors.
        val consl = cc.target.constructors
        if (consl.size != 0) {
            // if we are going to add constructors with parameters,
            // first we need to have a default constructor.
            cc.implClass._constructor()
        }
        run {
            // collision check
            val name: String = cc.target.squeezedName
            val existing: ClassOutlineImpl? = valueFactoryNames.put(name, cc)
            if (existing != null) {
                outline.errorReceiver.error(
                    existing.target.locator,
                    Messages.OBJECT_FACTORY_CONFLICT.format(name)
                )
                outline.errorReceiver.error(
                    cc.target.locator,
                    Messages.OBJECT_FACTORY_CONFLICT_RELATED.format()
                )
                return
            }
        }
        for (cons: Constructor in consl) {
            // method on ObjectFactory
            // [RESULT]
            // Foo createFoo( T1 a, T2 b, T3 c, ... ) throws JAXBException {
            //    return new FooImpl(a,b,c,...);
            // }
            val m = objectFactory._companion()._fun("create" + cc.target.squeezedName) {
                returnType(cc.ref.use())
                assign { cc.implRef._new {} }
            }

            // constructor
            // [RESULT]
            // FooImpl( T1 a, T2 b, T3 c, ... ) {
            // }
            val c = cc.implClass._constructor()
            for (fieldName: String in cons.fields) {
                val field = cc.target.getProperty(fieldName)
                if (field == null) {
                    outline.errorReceiver.error(
                        cc.target.locator,
                        Messages.ILLEGAL_CONSTRUCTOR_PARAM.format(fieldName)
                    )
                    continue
                }
                val newFieldName = camelize(fieldName)
                val fo = outline.getField(field)
                val accessor = fo.create(JExpr._this())

                // declare a parameter on this factory method and set
                // it to the field
                //inv.arg(m.param(fo.rawType, newFieldName))
                val variable = c.param(newFieldName) { type(fo.rawType.toKotlin()) }
                accessor.fromRawValue(c.block, "_$newFieldName", variable)
            }
        }
    }

    companion object {
        /** Change the first character to the lower case.  */
        private fun camelize(s: String): String {
            return s[0].lowercaseChar().toString() + s.substring(1)
        }
    }

}

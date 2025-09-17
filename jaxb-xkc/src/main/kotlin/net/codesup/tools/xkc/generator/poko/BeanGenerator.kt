package net.codesup.tools.xkc.generator.poko

import com.sun.codemodel.JClassAlreadyExistsException
import com.sun.codemodel.JPackage
import com.sun.istack.NotNull
import com.sun.tools.xjc.AbortException
import com.sun.tools.xjc.ErrorReceiver
import com.sun.tools.xjc.generator.annotation.spec.*
import com.sun.tools.xjc.model.*
import com.sun.xml.xsom.XmlString
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.JAXBException
import jakarta.xml.bind.annotation.XmlAttachmentRef
import jakarta.xml.bind.annotation.XmlID
import jakarta.xml.bind.annotation.XmlIDREF
import net.codesup.emit.SourceBuilder
import net.codesup.emit.SourceFile
import net.codesup.emit.declaration.ClassDeclaration
import net.codesup.emit.declaration.DeclarationOwner
import net.codesup.emit.declaration.PackageDeclaration
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.TypeUse
import net.codesup.tools.xkc.util.CodeModelClassFactory
import net.codesup.tools.xkc.model.*
import org.glassfish.jaxb.core.v2.model.core.ID
import org.glassfish.jaxb.core.v2.runtime.SwaRefAdapterMarker
import java.io.Serializable
import java.util.*
import javax.xml.namespace.QName


/**
 * Generates fields and accessors.
 */
class BeanGenerator constructor(
    /** the model object which we are processing.  */
    override val model: Model,
    override val errorReceiver: ErrorReceiver
) : Outline {
    /** Simplifies class/interface creation and collision detection.  */
    override val classFactory: CodeModelClassFactory

    /** all [PackageOutline]s keyed by their [PackageOutline._package].  */
    private val packageContexts: MutableMap<PackageDeclaration, PackageOutline> = LinkedHashMap()

    /** all [ClassOutline]s keyed by their [ClassOutline.target].  */
    val classMap: MutableMap<CClassInfo, ClassOutline> = LinkedHashMap()

    /** all [EnumOutline]s keyed by their [EnumOutline.target].  */
    val enumMap: MutableMap<CEnumLeafInfo, EnumOutline> = LinkedHashMap()

    /**
     * Generated runtime classes.
     */
    private val generatedRuntime: MutableMap<Class<*>, ClassTypeUse> = LinkedHashMap()
    override val codeModel: SourceBuilder

    /**
     * for each property, the information about the generated field.
     */
    private val fields: MutableMap<CPropertyInfo, FieldOutline> = LinkedHashMap()

    /**
     * elements that generate classes to the generated classes.
     */
    /*package*/
    val elements: Map<CElementInfo, ElementOutlineImpl> = LinkedHashMap()

    /**
     * Generates a class that knows how to create an instance of JAXBContext
     *
     *
     *
     * This is used in the debug mode so that a new properly configured
     * [JAXBContext] object can be used.
     */
    private fun generateClassList() {
        try {
            val jc = codeModel.rootPackage()._class("JAXBDebug")
            val m = jc.method(
                JMod.PUBLIC or JMod.STATIC,
                JAXBContext::class.java, "createContext"
            )
            val classLoader = m.param(ClassLoader::class.java, "classLoader")
            m._throws(JAXBException::class.java)
            val inv = codeModel.ref(JAXBContext::class.java).staticInvoke("newInstance")
            m.body()._return(inv)
            when (model.strategy) {
                ImplStructureStrategy.INTF_AND_IMPL -> {
                    val buf = StringBuilder()
                    for (po: PackageOutline in packageContexts.values) {
                        if (buf.length > 0) {
                            buf.append(':')
                        }
                        buf.append(po._package().name())
                    }
                    inv.arg(buf.toString()).arg(classLoader)
                }
                ImplStructureStrategy.BEAN_ONLY -> {
                    for (cc: ClassOutline in getClasses()) {
                        inv.arg(cc.implRef.dotClass())
                    }
                    for (po: PackageOutline in packageContexts.values) {
                        inv.arg(po.objectFactory().dotclass())
                    }
                }
                else -> throw IllegalStateException()
            }
        } catch (e: JClassAlreadyExistsException) {
            e.printStackTrace()
            // after all, we are in the debug mode. a little sloppiness is OK.
            // this error is not fatal. just continue.
        }
    }

    fun getContainer(parent: CClassInfoParent, aspect: Aspect): DeclarationOwner<*> {
        val v: CClassInfoParent.Visitor<DeclarationOwner<*>>
        when (aspect) {
            Aspect.EXPOSED -> v = exposedContainerBuilder
            Aspect.IMPLEMENTATION -> v = implContainerBuilder
            else -> {
                assert(false)
                throw IllegalStateException()
            }
        }
        return parent.accept(v)
    }

    fun resolve(ref: CTypeRef, a: Aspect): TypeUse {
        return ref.target.type.toType(this, a)
    }

    private val exposedContainerBuilder: CClassInfoParent.Visitor<DeclarationOwner<*>> =
        object : CClassInfoParent.Visitor<DeclarationOwner<*>> {
            override fun onBean(bean: CClassInfo): DeclarationOwner<*> {
                return getClazz(bean).ref
            }

            override fun onElement(element: CElementInfo): DeclarationOwner<*> {
                // hmm...
                return getElement(element).implClass
            }

            override fun onPackage(pkg: JPackage): DeclarationOwner<*> {
                return model.strategy.getPackage(pkg, Aspect.EXPOSED)
            }
        }
    private val implContainerBuilder: CClassInfoParent.Visitor<DeclarationOwner<*>> =
        object : CClassInfoParent.Visitor<DeclarationOwner<*>> {
            override fun onBean(bean: CClassInfo): DeclarationOwner<*> {
                return getClazz(bean).implClass
            }

            override fun onElement(element: CElementInfo): DeclarationOwner<*> {
                return getElement(element).implClass
            }

            override fun onPackage(pkg: JPackage): DeclarationOwner<*> {
                return model.strategy.getPackage(pkg, Aspect.IMPLEMENTATION)
            }
        }

    init {
        codeModel = model.codeModel
        classFactory = CodeModelClassFactory(errorReceiver)

        // build enum classes
        for (p: CEnumLeafInfo in model.enums().values) {
            enumMap[p] = generateEnumDef(p)
        }
        val packages = getUsedPackages(Aspect.EXPOSED)

        // generates per-package code and remember the results as contexts.
        for (pkg: JPackage in packages) {
            getPackageContext(pkg)
        }

        // create the class definitions for all the beans first.
        // this should also fill in PackageContext#getClasses
        for (bean: CClassInfo in model.beans().values) {
            getClazz(bean)
        }

        // compute the package-level setting
        for (p: PackageOutline? in packageContexts.values) {
            p!!.calcDefaultValues()
        }
        val OBJECT = codeModel.ref(Any::class.java)

        // inheritance relationship needs to be set before we generate fields, or otherwise
        // we'll fail to compute the correct type signature (namely the common base type computation)
        for (cc: ClassOutline in getClasses()) {

            // setup inheritance between implementation hierarchy.
            val superClass = cc.target.baseClass
            if (superClass != null) {
                // use the specified super class
                model.strategy._extends(cc, getClazz(superClass))
            } else {
                val refSuperClass = cc.target.refBaseClass
                if (refSuperClass != null) {
                    cc.implClass._extends(refSuperClass.toType(this, Aspect.EXPOSED))
                } else {
                    // use the default one, if any
                    if (model.rootClass != null && cc.implClass._extends() == OBJECT) {
                        cc.implClass._extends(model.rootClass)
                    }
                    if (model.rootInterface != null) {
                        cc.ref._implements(model.rootInterface)
                    }
                }
            }

            // if serialization support is turned on, generate
            // [RESULT]
            // class ... implements Serializable {
            //     private static final long serialVersionUID = <id>;
            //     ....
            // }
            if (model.serializable) {
                cc.implClass._implements(Serializable::class.java)
                if (model.serialVersionUID != null) {
                    cc.implClass.field(
                        JMod.PRIVATE or JMod.STATIC or JMod.FINAL,
                        codeModel.LONG,
                        "serialVersionUID",
                        JExpr.lit(model.serialVersionUID)
                    )
                }
            }
            val base = cc.target.parent()
            if (base != null && base is CClassInfo) {
                val pkg = base.getOwnerPackage().name()
                val shortName = base.fullName().substring(base.fullName().indexOf(pkg) + pkg.length + 1)
                if (cc.target.shortName == shortName) {
                    getErrorReceiver().error(cc.target.locator, Messages.ERR_KEYNAME_COLLISION.format(shortName))
                }
            }
        }

        // fill in implementation classes
        for (co: ClassOutlineImpl in getClasses()) {
            generateClassBody(co)
        }
        for (eo: EnumOutline in enums.values) {
            generateEnumBody(eo)
        }

        // create factories for the impl-less elements
        for (ei: CElementInfo in model.allElements) {
            getPackageContext(ei._package()).objectFactoryGenerator().populate(ei)
        }
        if (model.options.moduleName != null) {
            codeModel._prepareModuleInfo(model.options.moduleName, JAXB_PACKAGE)
        }
        if (model.options.debugMode) {
            generateClassList()
        }
    }

    /**
     * Returns all *used* JPackages.
     *
     * A JPackage is considered as "used" if a ClassItem or
     * a InterfaceItem resides in that package.
     *
     * This value is dynamically calculated every time because
     * one can freely remove ClassItem/InterfaceItem.
     *
     * @return
     * Given the same input, the order of packages in the array
     * is always the same regardless of the environment.
     */
    fun getUsedPackages(aspect: Aspect): Array<JPackage> {
        val s: MutableSet<JPackage> = TreeSet()
        for (bean: CClassInfo in model.beans().values) {
            val cont = getContainer(bean.parent(), aspect)
            if (cont.isPackage) {
                s.add(cont as JPackage)
            }
        }
        for (e: CElementInfo in model.getElementMappings(null).values) {
            // at the first glance you might think we should be iterating all elements,
            // not just global ones, but if you think about it, local ones live inside
            // another class, so those packages are already enumerated when we were
            // walking over CClassInfos.
            s.add(e._package())
        }
        return s.toTypedArray()
    }

    override fun getPackageContext(p: PackageDeclaration): PackageOutlineImpl {
        var r = packageContexts[p]
        if (r == null) {
            r = PackageOutlineImpl(this, model, p)
            packageContexts[p] = r
        }
        return r
    }

    /**
     * Generates the minimum [JDefinedClass] skeleton
     * without filling in its body.
     */
    private fun generateClassDef(bean: CClassInfo): ClassOutlineImpl {
        val r = model.strategy.createClasses(this, bean)
        val implRef: ClassTypeUse
        if (bean.userSpecifiedImplClass != null) {
            // create a place holder for a user-specified class.
            var usr: ClassDeclaration
            try {
                usr = codeModel._class(bean.userSpecifiedImplClass)
                // but hide that file so that it won't be generated.
                usr.hide()
            } catch (e: JClassAlreadyExistsException) {
                // it's OK for this to collide.
                usr = e.existingClass
            }
            usr._extends(r.implementation)
            implRef = usr
        } else {
            implRef = r.implementation
        }
        return ClassOutlineImpl(this, bean, r.exposed, r.implementation, implRef)
    }

    override val classes: Collection<ClassOutline> get() {
        // make sure that classes are fully populated
        assert(model.beans().size == classes.size)
        return classMap.values
    }

    override fun getClazz(bean: CClassInfo): ClassOutlineImpl {
        var r = classMap[bean]
        if (r == null) {
            classMap[bean] = generateClassDef(bean).also { r = it }
        }
        return r!!
    }

    override fun getElement(ei: CElementInfo): ElementOutlineImpl {
        var def = elements[ei]
        if (def == null && ei.hasClass()) {
            // create one. in the constructor it adds itself to the elements.
            def = ElementOutlineImpl(this, ei)
        }
        return def!!
    }

    override fun getEnum(eli: CEnumLeafInfo): EnumOutline {
        return enumMap[eli]!!
    }

    override val enums: Collection<EnumOutline> get() = enumMap.values

    override val allPackageContexts: Iterable<PackageOutline> get() = packageContexts.values

    override fun getField(prop: CPropertyInfo): FieldOutline {
        return fields[prop]!!
    }

    /**
     * Generates the body of a class.
     *
     */
    private fun generateClassBody(cc: ClassOutlineImpl) {
        val target = cc.target

        // used to simplify the generated annotations
        val mostUsedNamespaceURI = cc._package().mostUsedNamespaceURI

        // [RESULT]
        // @XmlType(name="foo", targetNamespace="bar://baz")
        val xtw = cc.implClass.annotate2(
            XmlTypeWriter::class.java
        )
        writeTypeName(cc.target.typeName, xtw, mostUsedNamespaceURI)

        // @XmlSeeAlso
        val subclasses = cc.target.listSubclasses()
        if (subclasses.hasNext()) {
            val saw = cc.implClass.annotate2(
                XmlSeeAlsoWriter::class.java
            )
            while (subclasses.hasNext()) {
                val s = subclasses.next()
                saw.value(getClazz(s).implRef)
            }
        }
        if (target.isElement) {
            val namespaceURI = target.elementName.namespaceURI
            val localPart = target.elementName.localPart

            // [RESULT]
            // @XmlRootElement(name="foo", targetNamespace="bar://baz")
            val xrew = cc.implClass.annotate2(
                XmlRootElementWriter::class.java
            )
            xrew.name(localPart)
            if (namespaceURI != mostUsedNamespaceURI) // only generate if necessary
            {
                xrew.namespace(namespaceURI)
            }
        }
        if (target.isOrdered) {
            for (p: CPropertyInfo in target.properties) {
                if (p !is CAttributePropertyInfo) {
                    if (!(p is CReferencePropertyInfo
                                && p.isDummy)
                    ) {
                        xtw.propOrder(p.getName(false))
                    }
                }
            }
        } else {
            // produce empty array
            xtw.annotationUse.paramArray("propOrder")
        }
        for (prop: CPropertyInfo in target.properties) {
            generateFieldDecl(cc, prop)
        }
        if (target.declaresAttributeWildcard()) {
            generateAttributeWildcard(cc)
        }

        // generate some class level javadoc
        cc.ref.javadoc().append(target.javadoc)
        cc._package().objectFactoryGenerator().populate(cc)
    }

    private fun writeTypeName(typeName: QName?, xtw: XmlTypeWriter, mostUsedNamespaceURI: String) {
        if (typeName == null) {
            xtw.name("")
        } else {
            xtw.name(typeName.localPart)
            val typeNameURI = typeName.namespaceURI
            if (typeNameURI != mostUsedNamespaceURI) // only generate if necessary
            {
                xtw.namespace(typeNameURI)
            }
        }
    }

    /**
     * Generates an attribute wildcard property on a class.
     */
    private fun generateAttributeWildcard(cc: ClassOutlineImpl) {
        val FIELD_NAME = "otherAttributes"
        val METHOD_SEED = model.nameConverter.toClassName(FIELD_NAME)
        val mapType = codeModel.ref(MutableMap::class.java).narrow(
            QName::class.java,
            String::class.java
        )
        val mapImpl = codeModel.ref(HashMap::class.java).narrow(
            QName::class.java,
            String::class.java
        )

        // [RESULT]
        // Map<QName,String> m = new HashMap<QName,String>();
        val `$ref` = cc.implClass.field(
            JMod.PRIVATE,
            mapType, FIELD_NAME, JExpr._new(mapImpl)
        )
        `$ref`.annotate2(XmlAnyAttributeWriter::class.java)
        val writer = cc.createMethodWriter()
        val `$get` = writer.declareMethod(mapType, "get$METHOD_SEED")
        `$get`.javadoc().append(
            "Gets a map that contains attributes that aren't bound to any typed property on this class.\n\n"
                    + "<p>\n"
                    + "the map is keyed by the name of the attribute and \n"
                    + "the value is the string value of the attribute.\n"
                    + "\n"
                    + "the map returned by this method is live, and you can add new attribute\n"
                    + "by updating the map directly. Because of this design, there's no setter.\n"
        )
        `$get`.javadoc().addReturn().append("always non-null")
        `$get`.body()._return(`$ref`)
    }

    /**
     * Generates the minimum [JDefinedClass] skeleton
     * without filling in its body.
     */
    private fun generateEnumDef(e: CEnumLeafInfo): EnumOutline {
        val type: JDefinedClass
        type = classFactory.createClass(
            getContainer(e.parent, Aspect.EXPOSED), e.shortName, e.locator, ClassType.ENUM
        )
        type.javadoc().append(e.javadoc)
        return object : EnumOutline(e, type) {
            @NotNull
            override fun parent(): Outline {
                return this@BeanGenerator
            }
        }
    }

    private fun generateEnumBody(eo: EnumOutline) {
        val type = eo.clazz
        val e = eo.target
        val xtw = type.annotate2(
            XmlTypeWriter::class.java
        )
        writeTypeName(
            e.typeName, xtw,
            eo._package().mostUsedNamespaceURI
        )
        val cModel = model.codeModel

        // since constant values are never null, no point in using the boxed types.
        val baseExposedType = e.base.toType(this, Aspect.EXPOSED).unboxify()
        val baseImplType = e.base.toType(this, Aspect.IMPLEMENTATION).unboxify()
        val xew = type.annotate2(
            XmlEnumWriter::class.java
        )
        xew.value(baseExposedType)
        val needsValue = e.needsValueField()

        // for each member <m>,
        // [RESULT]
        //    <EnumName>(<deserializer of m>(<value>));
        val enumFieldNames: MutableSet<String> = HashSet() // record generated field names to detect collision
        for (mem: CEnumConstant in e.members) {
            val constName = mem.getName()
            if (!JJavaName.isJavaIdentifier(constName)) {
                // didn't produce a name.
                getErrorReceiver().error(
                    e.locator,
                    Messages.ERR_UNUSABLE_NAME.format(mem.lexicalValue, constName)
                )
            }
            if (!enumFieldNames.add(constName)) {
                getErrorReceiver().error(e.locator, Messages.ERR_NAME_COLLISION.format(constName))
            }

            // [RESULT]
            // <Const>(...)
            // ASSUMPTION: datatype is outline-independent
            val constRef = type.enumConstant(constName)
            if (needsValue) {
                constRef.arg(e.base.createConstant(this, XmlString(mem.lexicalValue)))
            }
            if (mem.lexicalValue != constName) {
                constRef.annotate2(XmlEnumValueWriter::class.java).value(mem.lexicalValue)
            }

            // set javadoc
            if (mem.javadoc != null) {
                constRef.javadoc().append(mem.javadoc)
            }
            eo.constants.add(object : EnumConstantOutline(mem, constRef) {})
        }
        if (needsValue) {
            // [RESULT]
            // final <valueType> value;
            val `$value` = type.field(JMod.PRIVATE or JMod.FINAL, baseExposedType, "value")

            // [RESULT]
            // public <valuetype> value() { return value; }
            type.method(JMod.PUBLIC, baseExposedType, "value").body()._return(`$value`)

            // [RESULT]
            // <constructor>(<valueType> v) {
            //     this.value=v;
            // }
            run {
                val m: JMethod = type.constructor(0)
                m.body().assign(`$value`, m.param(baseImplType, "v"))
            }

            // [RESULT]
            // public static <Const> fromValue(<valueType> v) {
            //   for( <Const> c : <Const>.values() ) {
            //       if(c.value == v)   // or equals
            //           return c;
            //   }
            //   throw new IllegalArgumentException(...);
            // }
            run {
                val m: JMethod =
                    type.method(JMod.PUBLIC or JMod.STATIC, type, "fromValue")
                val `$v`: JVar = m.param(baseExposedType, "v")
                val fe: JForEach = m.body().forEach(type, "c", type.staticInvoke("values"))
                val eq: JExpression
                if (baseExposedType.isPrimitive()) {
                    eq = fe.`var`().ref(`$value`).eq(`$v`)
                } else {
                    eq = fe.`var`().ref(`$value`).invoke("equals").arg(`$v`)
                }
                fe.body()._if(eq)._then()._return(fe.`var`())
                val ex: JInvocation =
                    JExpr._new(cModel.ref(IllegalArgumentException::class.java))
                val strForm: JExpression
                if (baseExposedType.isPrimitive()) {
                    strForm = cModel.ref(String::class.java).staticInvoke("valueOf").arg(`$v`)
                } else if (baseExposedType === cModel.ref(String::class.java)) {
                    strForm = `$v`
                } else {
                    strForm = `$v`.invoke("toString")
                }
                m.body()._throw(ex.arg(strForm))
            }
        } else {
            // [RESULT]
            // public String value() { return name(); }
            type.method(JMod.PUBLIC, String::class.java, "value").body()._return(JExpr.invoke("name"))

            // [RESULT]
            // public <Const> fromValue(String v) { return valueOf(v); }
            val m = type.method(JMod.PUBLIC or JMod.STATIC, type, "fromValue")
            m.body()._return(JExpr.invoke("valueOf").arg(m.param(String::class.java, "v")))
        }
    }

    /**
     * Determines the FieldRenderer used for the given FieldUse,
     * then generates the field declaration and accessor methods.
     *
     * The `fields` map will be updated with the newly
     * created FieldRenderer.
     */
    private fun generateFieldDecl(cc: ClassOutlineImpl, prop: CPropertyInfo): FieldOutline {
        var fr = prop.realization
        if (fr == null) // none is specified. use the default factory
        {
            fr = model.options.fieldRendererFactory.default
        }
        val field = fr!!.generate(cc, prop)
        fields[prop] = field
        return field
    }

    /**
     * Generates [XmlJavaTypeAdapter] from [PropertyInfo] if necessary.
     * Also generates other per-property annotations
     * (such as [XmlID], [XmlIDREF], and [XmlMimeType] if necessary.
     */
    fun generateAdapterIfNecessary(prop: CPropertyInfo, field: JAnnotatable) {
        val adapter = prop.adapter
        if (adapter != null) {
            if (adapter.adapterIfKnown == SwaRefAdapterMarker::class.java) {
                field.annotate(XmlAttachmentRef::class.java)
            } else {
                // [RESULT]
                // @XmlJavaTypeAdapter( Foo.class )
                val xjtw = field.annotate2(
                    XmlJavaTypeAdapterWriter::class.java
                )
                xjtw.value(adapter.adapterType.toType(this, Aspect.EXPOSED))
            }
        }
        when (prop.id()) {
            ID.ID -> field.annotate(XmlID::class.java)
            ID.IDREF -> field.annotate(XmlIDREF::class.java)
        }
        if (prop.expectedMimeType != null) {
            field.annotate2(XmlMimeTypeWriter::class.java).value(prop.expectedMimeType.toString())
        }
    }

    override fun addRuntime(clazz: Class<*>): ClassTypeUse {
        var g = generatedRuntime[clazz]
        if (g == null) {
            // put code into a separate package to avoid name conflicts.
            val implPkg = getUsedPackages(Aspect.IMPLEMENTATION)[0].subPackage("runtime")
            g = generateStaticClass(clazz, implPkg)
            generatedRuntime[clazz] = g
        }
        return g
    }

    fun generateStaticClass(src: Class<*>, out: JPackage): ClassTypeUse {
        val sjf = SourceFile(out, getShortName(src), src, null)
        out.addResourceFile(sjf)
        return sjf.jClass
    }

    private fun getShortName(src: Class<*>): String {
        val name = src.name
        return name.substring(name.lastIndexOf('.') + 1)
    }

    companion object {
        /** JAXB module name. JAXB dependency is mandatory in generated Java module.  */
        private val JAXB_PACKAGE = "jakarta.xml.bind"

        /**
         * Generates beans into code model according to the BGM,
         * and produces the reflection model.
         *
         * @param _errorReceiver
         * This object will receive all the errors discovered
         * during the back-end stage.
         *
         * @return
         * returns a [Outline] which will in turn
         * be used to further generate marshaller/unmarshaller,
         * or null if the processing fails (errors should have been
         * reported to the error recevier.)
         */
        fun generate(model: Model, _errorReceiver: ErrorReceiver): Outline? {
            try {
                return BeanGenerator(model, _errorReceiver)
            } catch (e: AbortException) {
                return null
            }
        }
    }
}

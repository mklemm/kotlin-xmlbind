package net.codesup.tools.xkc.model

import com.sun.tools.xjc.generator.bean.ImplStructureStrategy

/**
 * Sometimes a single JAXB-generated bean spans across multiple Java classes/interfaces.
 * We call them "aspects of a bean".
 *
 *
 *
 * This is an enumeration of all possible aspects.
 *
 * @author Kohsuke Kawaguchi
 *
 * TODO: move this to the model package. We cannot do this before JAXB3 because of old plugins
 */
enum class Aspect {
    /**
     * The exposed part of the bean.
     *
     *
     * This corresponds to the content interface when we are geneting one.
     * This would be the same as the [.IMPLEMENTATION] when we are
     * just generating beans.
     *
     *
     *
     * This could be an interface, or it could be a class.
     *
     * We don't have any other [ImplStructureStrategy] at this point,
     * but generally you can't assume anything about where this could be
     * or whether that's equal to [.IMPLEMENTATION].
     */
    EXPOSED,

    /**
     * The part of the bean that holds all the implementations.
     *
     *
     *
     * This is always a class, never an interface.
     */
    IMPLEMENTATION
}

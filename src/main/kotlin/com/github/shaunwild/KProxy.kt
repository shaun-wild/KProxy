package com.github.shaunwild

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinFunction

/**
 * A lightweight wrapper around the [Proxy] API.
 * @author shaun-wild
 * */
object KProxy {
    /**
     * Creates a new proxy instance of the given interface.
     * @param kInvocationHandler The [KInvocationHandler] instance to handle the invocations.
     * @param loader The classloader to use.
     * @param T The interface class being proxied.
     * @throws IllegalArgumentException if any of the restrictions on the parameters that may be passed to getProxyClass are violated
     * @throws SecurityException if a security manager, s, is present and any of the following conditions is met:
     * @see Proxy.newProxyInstance
     * */
    inline fun <reified T : Any> newProxyInstance(
        kInvocationHandler: KInvocationHandler,
        loader: ClassLoader = ClassLoader.getSystemClassLoader(),
    ) = newProxyInstance(kInvocationHandler, T::class, loader)

    /**
     * Creates a new proxy instance of the given interface.
     * @param kInvocationHandler The [KInvocationHandler] instance to handle the invocations.
     * @param loader The classloader to use.
     * @param kClass The interface class being proxied.
     * @throws IllegalArgumentException if any of the restrictions on the parameters that may be passed to getProxyClass are violated
     * @throws SecurityException if a security manager, s, is present and any of the following conditions is met:
     * @see Proxy.newProxyInstance
     * */
    fun <T : Any> newProxyInstance(
        kInvocationHandler: KInvocationHandler,
        kClass: KClass<T>,
        loader: ClassLoader = ClassLoader.getSystemClassLoader()
    ) = Proxy.newProxyInstance(
        loader,
        arrayOf(kClass.java),
        KInvocationHandlerAdapter(kInvocationHandler, kClass)
    ) as T

    /**
     * Returns true if the given [kClass] is a proxy.
     * @see Proxy.isProxyClass
     * */
    fun isProxyClass(kClass: KClass<*>) =
        Proxy.isProxyClass(kClass.java)

    /**
     * Returns the proxy class for the given interfaces.
     * @see Proxy.getProxyClass
     * */
    fun getProxyClass(vararg interfaces: KClass<*>) =
        Proxy.getProxyClass(ClassLoader.getSystemClassLoader(), *interfaces.map(KClass<*>::java).toTypedArray()).kotlin
}

/**
 * A Kotlin aware [InvocationHandler], it will map the java interface
 * proxied calls to our Kotlin specific members.
 * */
interface KInvocationHandler {

    /**
     * Called whenever a `val` or `var` property is read.
     * @param proxy The proxy instance.
     * @param kProperty The [KProperty] that was read.
     * @return The proxy value of the property.
     * */
    fun invokeKPropertyRead(proxy: Any?, kProperty: KProperty<*>): Any?

    /**
     * Called whenever a `var` property is written to.
     * @param proxy The proxy instance.
     * @param kMutableProperty The [KMutableProperty] that was written to.
     * @return The proxy value to be returned from getting the property.
     * */
    fun invokeKPropertyWrite(proxy: Any?, kMutableProperty: KMutableProperty<*>, value: Any?)

    /**
     * Called when any other `fun` is called.
     * @param proxy The proxy instance.
     * @param kFunction The [KFunction] that was called.
     * @return The proxy value to be returned from the function.
     * */
    fun invokeKFunction(proxy: Any?, kFunction: KFunction<*>, args: Array<out Any?>?): Any?
}

private class KInvocationHandlerAdapter(
    val kInvocationHandler: KInvocationHandler,
    val kClass: KClass<*>
) : InvocationHandler {

    private val methodGetterMap = kClass.memberProperties
        .associateBy { it.getter.javaMethod }

    private val methodSetterMap = kClass.memberProperties
        .filterIsInstance<KMutableProperty<*>>()
        .associateBy { it.setter.javaMethod }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        methodGetterMap[method]?.let {
            return kInvocationHandler.invokeKPropertyRead(proxy, it)
        }

        methodSetterMap[method]?.let {
            return kInvocationHandler.invokeKPropertyWrite(proxy, it, args?.firstOrNull())
        }

        return kInvocationHandler.invokeKFunction(proxy, method.kotlinFunction!!, args)
    }
}

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

object KProxy {
    inline fun <reified T> newProxyInstance(
        kInvocationHandler: KInvocationHandler,
        loader: ClassLoader = ClassLoader.getSystemClassLoader(),
    ) = Proxy.newProxyInstance(
        loader,
        arrayOf(T::class.java),
        KInvocationHandlerAdapter(kInvocationHandler, T::class)
    ) as T

    fun <T : Any> newProxyInstance(
        kInvocationHandler: KInvocationHandler,
        kClass: KClass<T>,
        loader: ClassLoader = ClassLoader.getSystemClassLoader()
    ) = Proxy.newProxyInstance(
        loader,
        arrayOf(kClass.java),
        KInvocationHandlerAdapter(kInvocationHandler, kClass)
    ) as T

    fun isProxyClass(kClass: KClass<*>) =
        Proxy.isProxyClass(kClass.java)

    fun getProxyClass(vararg interfaces: KClass<*>) =
        Proxy.getProxyClass(ClassLoader.getSystemClassLoader(), *interfaces.map(KClass<*>::java).toTypedArray()).kotlin
}

class KInvocationHandlerAdapter(
    val kInvocationHandler: KInvocationHandler,
    val kClass: KClass<*>
) : InvocationHandler {

    private val methodMutablePropertyMap = kClass.memberProperties
        .filterIsInstance<KMutableProperty<*>>()
        .flatMap { listOf(it.getter.javaMethod to it, it.setter.javaMethod to it) }
        .toMap()

    private val methodPropertyMap = kClass.memberProperties
        .filter { it !is KMutableProperty<*> }
        .associateBy { it.getter.javaMethod }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        methodMutablePropertyMap[method]?.let {
            return kInvocationHandler.invokeKMutableProperty(proxy, it, args?.first())
        }

        methodPropertyMap[method]?.let {
            return kInvocationHandler.invokeKProperty(proxy, it)
        }

        return kInvocationHandler.invokeKFunction(proxy, method.kotlinFunction!!, args)
    }
}

interface KInvocationHandler {
    fun invokeKProperty(proxy: Any?, kProperty: KProperty<*>): Any?
    fun invokeKMutableProperty(proxy: Any?, kMutableProperty: KMutableProperty<*>, value: Any?): Any?
    fun invokeKFunction(proxy: Any?, kFunction: KFunction<*>, args: Array<out Any?>?): Any?
}

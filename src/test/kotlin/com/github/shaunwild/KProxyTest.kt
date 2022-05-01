package com.github.shaunwild

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

internal class KProxyTest {

    @Test
    fun newProxyInstance_createsProxyInstance() {
        val proxy = KProxy.newProxyInstance<TestInterface>(TestInvocationHandler)

        assertEquals(KPROPERTY, proxy.foo)
        assertEquals(KMUTABLE, proxy.bar)
        assertEquals(KFUNCTION, proxy.getBaz())
    }

    @Test
    fun nonGeneric_newProxyInstance_createsProxyInstance() {
        val proxy = KProxy.newProxyInstance(TestInvocationHandler, TestInterface::class)

        assertEquals(KPROPERTY, proxy.foo)
        assertEquals(KMUTABLE, proxy.bar)
        assertEquals(KFUNCTION, proxy.getBaz())
    }

    @Test
    fun isProxyClass_isProxyClass_returnsTrue() {
        val proxy = KProxy.newProxyInstance<TestInterface>(TestInvocationHandler)
        assertTrue(KProxy.isProxyClass(proxy::class))
    }

    @Test
    fun getProxyClass_returnsProxyClass() {
        val proxy = KProxy.newProxyInstance<TestInterface>(TestInvocationHandler)
        assertEquals(proxy::class, KProxy.getProxyClass(TestInterface::class))
    }

    interface TestInterface {
        val foo: String
        var bar: Int

        fun getBaz(): String
    }

    object TestInvocationHandler : KInvocationHandler {
        override fun invokeKProperty(proxy: Any?, kProperty: KProperty<*>): Any? {
            return KPROPERTY
        }

        override fun invokeKMutableProperty(proxy: Any?, kMutableProperty: KMutableProperty<*>, value: Any?): Any? {
            return KMUTABLE
        }

        override fun invokeKFunction(proxy: Any?, kFunction: KFunction<*>, args: Array<out Any?>?): Any? {
            return KFUNCTION
        }
    }

    companion object {
        const val KPROPERTY = "KPROPERTY"
        const val KMUTABLE = 25
        const val KFUNCTION = "KFUNCTION"
    }
}

package com.github.shaunwild

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class KProxyTest {

    private val proxyMock = mockk<KInvocationHandler>()
    private val proxy = KProxy.newProxyInstance<TestInterface>(proxyMock)

    @Test
    fun kInvocationHandler_invokeProperty() {
        every { proxyMock.invokeKPropertyRead(any(), any()) } returns KPROPERTY
        assertEquals(KPROPERTY, proxy.foo)
        assertEquals(KPROPERTY, proxy.bar)
        verify(atLeast = 2) { proxyMock.invokeKPropertyRead(any(), any()) }
    }

    @Test
    fun kInvocationHandler_invokeKFunction_returnsProxyValue() {
        every { proxyMock.invokeKFunction(any(), any(), any()) } returns KFUNCTION
        assertEquals(KFUNCTION, proxy.getBaz())
        verify { proxyMock.invokeKFunction(any(), any(), any()) }
    }

    @Test
    fun kInvocationHandler_newProxyInstance_createsProxyInstance() {
        val proxy = KProxy.newProxyInstance(proxyMock, TestInterface::class)

        every { proxyMock.invokeKPropertyRead(any(), any()) } returns KPROPERTY
        every { proxyMock.invokeKFunction(any(), any(), any()) } returns KFUNCTION

        assertEquals(KPROPERTY, proxy.foo)
        assertEquals(KPROPERTY, proxy.bar)
        assertEquals(KFUNCTION, proxy.getBaz())
    }

    @Test
    fun kInvocationHandler_setProperty_invokesSetter() {
        every { proxyMock.invokeKPropertyWrite(any(), any(), any()) } returns Unit
        proxy.bar = KMUTATE
        verify { proxyMock.invokeKPropertyWrite(any(), any(), any()) }
    }

    @Test
    fun isProxyClass_isProxyClass_returnsTrue() {
        assertTrue(KProxy.isProxyClass(proxy::class))
    }

    @Test
    fun getProxyClass_returnsProxyClass() {
        assertEquals(proxy::class, KProxy.getProxyClass(TestInterface::class))
    }

    interface TestInterface {
        val foo: String
        var bar: String

        fun getBaz(): String
    }

    companion object {
        const val KMUTATE = "KMUTATE"
        const val KPROPERTY = "KPROPERTY"
        const val KMUTABLE = 25
        const val KFUNCTION = "KFUNCTION"
    }
}

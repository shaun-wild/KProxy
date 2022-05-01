# KProxy

A lightweight Kotlin wrapper of the Proxy API.

## Usage

Declare your interface:

```kotlin
interface MyInterface {
    val foo: String
}
```

Create your `KInvocationHandler`:

```kotlin
object TestInvocationHandler : KInvocationHandler {
    override fun invokeKProperty(proxy: Any?, kProperty: KProperty<*>): Any? {
        TODO("Add my KProperty implementation!")
    }

    override fun invokeKMutableProperty(proxy: Any?, kMutableProperty: KMutableProperty<*>, value: Any?): Any? {
        TODO("Add my KMutableProperty implementation!")
    }

    override fun invokeKFunction(proxy: Any?, kFunction: KFunction<*>, args: Array<out Any?>?): Any? {
        TODO("Add my KFunction implementation!")
    }
}
```

Create the proxy!

```kotlin
KProxy.newProxyInstance<MyInterface>(TestInvocationHandler)
```

## License

MIT - Give me a shoutout if you like! 🚀

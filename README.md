# KProxy

A lightweight Kotlin wrapper of the Proxy API.

[![](https://jitpack.io/v/shaun-wild/KProxy.svg)](https://jitpack.io/#shaun-wild/KProxy)

-----

## Installation

Add jitpack to your repositories.

```kotlin
repositories {
    maven("https://jitpack.io")
}
```

Add the dependency:

```kotlin
implementation("com.github.shaun-wild:KProxy:[version]")
```

## Usage

Declare your interface:

```kotlin
interface MyInterface {
    val foo: String
}
```

Create your `KInvocationHandler`:

```kotlin
class MyInvocationHandler : KInvocationHandler {
    override fun invokeKPropertyRead(proxy: Any?, kProperty: KProperty<*>): Any? {
        TODO("Not yet implemented")
    }

    override fun invokeKPropertyWrite(proxy: Any?, kMutableProperty: KMutableProperty<*>, value: Any?) {
        TODO("Not yet implemented")
    }

    override fun invokeKFunction(proxy: Any?, kFunction: KFunction<*>, args: Array<out Any?>?): Any? {
        TODO("Not yet implemented")
    }
}
```

Create the proxy!

```kotlin
KProxy.newProxyInstance<MyInterface>(TestInvocationHandler)
```

*This project uses Kotlin version 1.6.10*

## License

MIT - Give me a shout-out if you like this! 🚀

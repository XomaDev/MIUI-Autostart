# MIUI Autostart

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)]()
[![kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)]()

A library to check MIUI autostart permission state.

### Tested on

- MIUI 10 (firebase)
- MIUI 11 (physical device 11.0.9)
- MIUI 12 (physical device 12.5)
- MIUI 13 (untested, but works)
- MIUI 14 (physical device 14.0.2)


<b>But not limited for newer releases</b>

<hr>

## Setup

### Gradle

Add the JitPack repository to your build file

```
repositories {
    maven { url 'https://jitpack.io' }
}
```

Add the dependency

```
dependencies {
    implementation 'com.github.XomaDev:MIUI-autostart:v1.2'
}
```

### Maven

Add the JitPack repository to your build file

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency

```
<dependency>
    <groupId>com.github.XomaDev</groupId>
    <artifactId>MIUI-autostart</artifactId>
    <version>v1.2</version>
</dependency>
```

## Usage

It's recommended to use the Safe API, refer documentation

```kotlin
val enabled: Boolean = Autostart.getSafeState(context)
```
```java
boolean enabled = Autostart.INSTANCE.getSafeState(context);
```

Or using `isAutoStartEnabled(Context, DefaultValue)`

## Support

[![Paypal](https://img.shields.io/badge/PayPal-00457C?style=for-the-badge&logo=paypal&logoColor=white)](https://paypal.me/XomaDev)
[![Stripe](https://img.shields.io/badge/Stripe-626CD9?style=for-the-badge&logo=Stripe&logoColor=white)](https://buy.stripe.com/eVadUDdeY4ov3QIdR7)

I'm Kumaraswamy, also known as Xoma Dev. A self-taught developer with expertise in Kotlin and Java, I have a strong passion for learning new algorithms. Crafting Android projects is not just work for me, it's where I find my joy and creativity.

<b>Your support means a lot to me.</b><br>

Finding this solution was really tough. I had to work and research day after day until I finally figured it out. If you can help out with a small donation, it would mean a lot and add value to my work. Thanks a lot!

Kumaraswamy B.G

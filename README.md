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

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Add the dependency

```groovy
dependencies {
    implementation 'com.github.XomaDev:MIUI-autostart:v1.3'
}
```

### Maven

Add the JitPack repository to your build file

```html
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency

```html
<dependency>
    <groupId>com.github.XomaDev</groupId>
    <artifactId>MIUI-autostart</artifactId>
    <version>v1.3</version>
</dependency>
```

## Usage

It's recommended to use the Safe API; refer to the documentation

```kotlin
val enabled: Boolean = Autostart.getSafeState(context)
```
```java
boolean enabled = Autostart.INSTANCE.getSafeState(context);
```

Or using `isAutoStartEnabled(Context, DefaultValue)`

## Google Play policy

This library relies on hidden APIs in the MIUI framework to access the auto-start state. From Android 9 and later, access to hidden APIs is restricted. This library gets around that restriction.

Although I could not find an explicitly stated policy regarding the use of hidden APIs, based on reports, your application might not pass review unless you stop reporting library usage.

From _LSPosed/AndroidHiddenApiBypass_:

_Google Play doesn't allow apps to use hidden APIs, reporting library usage will cause your app to fail app review, you need to disable dependencies info reporting in build.gradle. Remember to update this library to latest version to be compatible with new Android version._


```gradle
android {
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}
```

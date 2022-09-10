# MIUI Autostart

A library to check MIUI autostart permission state.

### Supported versions

- MIUI 10 (firebase)
- MIUI 11 (physical device 11.0.9)
- MIUI 12 (physical device 12.5)
<hr>

## Setup

### Gradle

Add the JitPack repository to your build file

```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

Add the dependency

```
    dependencies {
        implementation 'com.github.XomaDev:MIUI-autostart:master-SNAPSHOT'
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
	    <groupId>com.github.XomaDev:MIUI-autostart</groupId>
	    <artifactId>Bubbles</artifactId>
	    <version>master-SNAPSHOT</version>
	</dependency>
```

## Usage

```java
// make sure device is MIUI device, else an 
// exception will be thrown at initialization
Autostart autostart = new Autostart(applicationContext);

State state = autostart.getAutoStartState();

if (state == State.DISABLED) {
    // now we are sure that autostart is disabled
    // ask user to enable it manually in the settings app    
} else if (state == State.ENABLED) {
    // now we are also sure that autostart is enabled
}
```

### White listed packages (by default)

```java
Autostart autostart = new Autostart(applicationContext);

// get the list of packages which are 
// white-listed by system by default
        
String[] packagesWhiteListed = autostart.defaultWhiteListedPackages();
```

Kumaraswamy B.G

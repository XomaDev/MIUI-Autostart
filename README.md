# MIUI Autostart

A library to check MIUI autostart permission state.

### Supported versions

- MIUI 10 (firebase)
- MIUI 11 (physical device 11.0.9)
- MIUI 12 (physical device 12.5)
<hr>

## Usage

Library (.jar) is present in `lib/` directory.
You can directly use it in your Android project.

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

<hr>

# Bypass Any OEM Restriction

There is actually a tricky way to bypass autostart permision in any OEM (probably all).

First we have to know that, like MIUI for e.g, performs check of autostart when the service is about to start and when the service restarts. We can use this behaviour to launch a Foreground state with a notification alive until the app is closed. When it does, you could simply remove the foreground by `stopForeground(bool)` method and the service will continue along. This isnt the good way (but it is worth noting it).

<hr>

Kumaraswamy B.G

/*
  MIUI autostart helper library
  <p>
  author: Kumaraswamy B.G (XomaDev)
  License: MIT license (refer license file)
 */


package xyz.kumaraswamy.autostart;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import me.weishu.reflection.Reflection;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings("unused")
public class Autostart {

    private static final String TAG = "Autostart";

    private static final String BUILD_PROP = "build.prop";
    private static final String MIUI_VERSION_CODE_PROP = "ro.miui.ui.version.code";
    private static final String MIUI_VERSION_NAME_PROP = "ro.miui.ui.version.name";

    private static final String MIUI_CLAZZ = "android.miui.AppOpsUtils";
    private static final String POLICY_CLAZZ = "miui.content.pm.PreloadedAppPolicy";

    private static boolean isReflectionEnabled = false;

    private static final int STATE_ENABLED = 0;
    private static final int STATE_DISABLED = 1;

    private final Context context;

    /**
     * State for the autostart
     * ENABLED and DISABLED are the expected results for a MIUI device
     */

    public enum State {
        ENABLED, DISABLED, NO_INFO, UNEXPECTED_RESULT
    }

    public static boolean isMIUI() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(new File(Environment.getRootDirectory(), BUILD_PROP)));
            String versionCode = props.getProperty(MIUI_VERSION_CODE_PROP, null);
            String versionName = props.getProperty(MIUI_VERSION_NAME_PROP, null);
            return versionCode != null || versionName != null;
        } catch (IOException e) {
            Log.e(TAG, "Can't access system directory");
        }
        return false;
    }

    /**
     * Create an instance of Autostart, the phone must be Xiaomi device
     *
     * @param context Application context
     * @throws Exception phone is not using MIUI!
     */

    public Autostart(Context context) throws Exception {
        if (!isMIUI()) {
            throw new Exception("Not a MIUI device");
        }
        this.context = context;

        if (!isReflectionEnabled) {
            Reflection.unseal(context);
            isReflectionEnabled = true;
        }
    }

    /**
     * Checks if autostart is enabled.
     * May also return false (even if its enabled)
     */

    public boolean isAutoStartEnabled() throws Exception {
        return getAutoStartState() == State.ENABLED;
    }

    /**
     * Returns the state of the autostart
     *
     * <p>
     * ENABLED / DISABLED = autostart state
     * NOINFO = something went wrong when attempting to get the state
     * UNEXPECTED_RESULT = we got the state, but it is not right
     * </p>
     */

    public State getAutoStartState() throws Exception {
        final Class<?> clazz = getClazz(MIUI_CLAZZ);

        if (clazz == null) {
            return State.NO_INFO;
        }

        final Method method = findMethod(clazz);

        if (method == null) {
            // the method does not exist!
            return State.NO_INFO;
        }

        final Object result = method.invoke(null,
                context, context.getPackageName());

        // it must be an integer, else things are changed

        if (!(result instanceof Integer)) {
            return State.UNEXPECTED_RESULT;
        }
        final int _int = (int) result;
        if (_int == STATE_ENABLED) {
            return State.ENABLED;
        } else if (_int == STATE_DISABLED) {
            return State.DISABLED;
        }
        return State.UNEXPECTED_RESULT;
    }

    @SuppressWarnings("SuspiciousToArrayCall")
    public String[] defaultWhiteListedPackages() throws NoSuchFieldException, IllegalAccessException {
        final Class<?> clazz = getClazz(POLICY_CLAZZ);
        if (clazz == null) {
            // just return an empty array
            return new String[0];
        }
        final Field field = clazz.getDeclaredField("sProtectedDataApps");
        field.setAccessible(true);

        // we pass null to `Field.get()` because the field
        // is statically defined
        final Object result = field.get(null);
        if (result instanceof ArrayList<?>) {
            return ((ArrayList<?>) result).toArray(new String[0]);
        }
        final String message = "defaultWhiteListedPackages() unexpected result type";
        if (result == null) {
            return new String[0];
        }
        Log.e(TAG, message + " " + result.getClass());
        return new String[0];
    }

    private Class<?> getClazz(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException ignored) {
            // we couldn't find the class name
            // we are out of luck
            return null;
        }
    }

    /**
     * Finds the method of the MIUI clazz
     *
     * @return returns the method, is null
     * if method is not found
     */


    private @Nullable Method findMethod(final Class<?> clazz) {
        final String methodName = "getApplicationAutoStart";

        try {
            final Method method = clazz.getDeclaredMethod("getApplicationAutoStart",
                    Context.class, String.class);

            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    Log.i(TAG, "Found a new method matching method name");
                }
            }
            // we could not find the method name
            // we were near to get the autostart
            return null;
        }
    }
}

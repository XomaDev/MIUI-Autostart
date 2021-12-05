/**
 * MIUI autostart helper library
 * <p>
 * author: Kumaraswamy B.G (XomaDev)
 * License: MIT license (refer license file)
 */


package xyz.kumaraswamy.autostart;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import me.weishu.reflection.Reflection;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class Autostart {

    private static final String TAG = "Autostart";

    private static final String XIAOMI_NAME = "xiaomi";

    private static final String MIUI_CLAZZ = "android.miui.AppOpsUtils";

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

    public static boolean isXiaomi() {
        return Build.MANUFACTURER.equalsIgnoreCase(XIAOMI_NAME);
    }

    /**
     * Create an instance of Autostart, the phone must be Xiaomi device
     *
     * @param context Application context
     * @throws Exception phone is not Xiaomi device!
     */

    public Autostart(Context context) throws Exception {
        if (!isXiaomi()) {
            throw new Exception("Not a Xiaomi device");
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
        final Class<?> clazz;
        try {
            clazz = Class.forName(MIUI_CLAZZ);
        } catch (ClassNotFoundException ignored) {
            // we couldn't find the class name
            // we are out of luck
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

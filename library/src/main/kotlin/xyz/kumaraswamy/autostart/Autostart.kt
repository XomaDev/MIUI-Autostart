/*
MIT License

Copyright (c) [2021] Kumaraswamy B G
Developer: XomaDev

See the accompanying LICENSE file for details.
*/

package xyz.kumaraswamy.autostart

import android.content.Context
import android.os.Build
import android.util.Log
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.reflect.Method
import kotlin.Exception

@Suppress("unused")
object Autostart {

  private const val TAG = "Autostart"

  private const val XIAOMI_NAME = "xiaomi"

  private const val MIUI_CLAZZ = "android.miui.AppOpsUtils"
  private const val POLICY_CLAZZ = "miui.content.pm.PreloadedAppPolicy"

  private const val STATE_ENABLED = 0
  private const val STATE_DISABLED = 1

  init {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      try {
        HiddenApiBypass.addHiddenApiExemptions("")
      } catch (ignored: Exception) {
        Log.d(TAG, "Failed to bypass API Exemption")
      }
    }
  }

  /**
   * Raw State for the autostart
   * ENABLED and DISABLED are the expected results for a MIUI device
   */
  enum class State {
    ENABLED, DISABLED, NO_INFO, UNEXPECTED_RESULT
  }

  /**
   * Recommended usage with `Utils.isOnMiui()`
   * Returns whether the Autostart is enabled or not;
   *
   * @Deprecated use isAutoStartEnabled(Context, Boolean) instead
   * @throws Exception if couldn't find the information
   */
  @Deprecated(message = "Use isAutoStartEnabled(Context, Boolean)")
  fun isAutostartEnabled(context: Context): Boolean {
    val state = getAutoStartState(context)
    if (state == State.ENABLED || state == State.DISABLED)
      return state == State.ENABLED
    throw Exception("Unhandled, unknown state")
  }

  /**
   * Recommended usage with `Utils.isOnMiui()`
   * Returns whether the Autostart is enabled or not;
   *
   * @param defaultValue Value to return on Unexpected result
   */

  fun isAutoStartEnabled(context: Context, defaultValue: Boolean = true): Boolean {
    val state = getAutoStartState(context)
    if (state == State.ENABLED || state == State.DISABLED)
      return state == State.ENABLED
    return defaultValue
  }

  /**
   * A safe method to check Autostart State, simply returns True
   * if Autostart state not available
   *
   * @return true when {Autostart Enabled (or) State Unavailable} and returns false
   * when Autostart is actually disabled
   */

  fun getSafeState(context: Context): Boolean {
    if (!Utils.isOnMiui())
      return true
    return isAutoStartEnabled(context, true)
  }

  /**
   * Returns the raw state of Autostart;
   * ENABLED; DISABLED; NO_INFO; UNEXPECTED_RESULT
   */

  fun getAutoStartState(context: Context): State {
    // let's find the MIUI AppOps Utils
    val clazz = getClazz(MIUI_CLAZZ)
      ?: return State.NO_INFO

    val method = findMethod(clazz)
      ?: // the method does not exist!
      return State.NO_INFO

    val result = method.invoke(
      null,
      context, context.packageName
    ) as? Int ?: return State.UNEXPECTED_RESULT

    // result must be an integer

    return when (result) {
      STATE_ENABLED -> {
        State.ENABLED
      }

      STATE_DISABLED -> {
        State.DISABLED
      }

      else -> State.UNEXPECTED_RESULT
    }
  }

  /**
   * Returns a list of whitelisted packages by default
   */

  fun defaultWhiteListedPackages(): Array<String?> {
    val clazz = getClazz(POLICY_CLAZZ)
      ?: // just return an empty array
      return arrayOfNulls(0)
    val field = clazz.getDeclaredField("sProtectedDataApps")
    field.isAccessible = true

    // we pass null to `Field.get()` because the field
    // is statically defined
    val result = field.get(null)
    if (result is ArrayList<*>) {
      return result.toArray(arrayOf<String>())
    }
    val message = "defaultWhiteListedPackages() unexpected result type"
    if (result == null) {
      return arrayOfNulls(0)
    }
    Log.e(TAG, message + " " + result.javaClass)
    return arrayOfNulls(0)
  }

  private fun getClazz(name: String): Class<*>? {
    return try {
      Class.forName(name)
    } catch (ignored: ClassNotFoundException) {
      // return; not found
      null
    }
  }

  /**
   * Finds the method of the MIUI clazz
   *
   * @return returns the method, is null
   * if method is not found
   */
  private fun findMethod(clazz: Class<*>): Method? {
    return try {
      val method = clazz.getDeclaredMethod(
        "getApplicationAutoStart",
        Context::class.java, String::class.java
      )
      method.isAccessible = true
      method
    } catch (e: NoSuchMethodException) {
      // we didn't find the method that we were
      // looking for
      null
    }
  }
}
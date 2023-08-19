package xyz.kumaraswamy.autostart

import android.annotation.SuppressLint
import android.text.TextUtils

@Suppress("unused")
object Utils {

  private const val SYSTEM_PROPS_CLASS = "android.os.SystemProperties"
  private const val MIUI_VERSION_PROPERTY = "ro.miui.ui.version.code"

  @SuppressLint("PrivateApi")
  fun isOnMiui(): Boolean {
    Class.forName(SYSTEM_PROPS_CLASS).apply {
      return !TextUtils.isEmpty(
        getMethod("get", String::class.java)
          .invoke(this, MIUI_VERSION_PROPERTY) as String
      )
    }
  }
}
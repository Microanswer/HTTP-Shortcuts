package ch.rmy.android.framework.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferencesStore(context: Context) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    protected fun getString(key: String): String? =
        preferences.getString(key, null)

    protected fun getBoolean(key: String): Boolean =
        preferences.getBoolean(key, false)

    protected fun getInt(key: String): Int? =
        preferences.getInt(key, Int.MIN_VALUE).takeUnless { it == Int.MIN_VALUE }

    protected fun getLong(key: String): Long? =
        preferences.getLong(key, Long.MIN_VALUE).takeUnless { it == Long.MIN_VALUE }

    protected fun putString(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    protected fun putBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    protected fun putLong(key: String, value: Long?) {
        preferences.edit().run {
            if (value != null) {
                putLong(key, value)
            } else {
                remove(key)
            }
        }.apply()
    }
}
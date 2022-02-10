package com.udacity

import android.content.Context
import androidx.preference.PreferenceManager

object AppUtils {
    fun getPrefBoolean(key: String, context: Context): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getBoolean(key, false)
    }

    fun putPref(key: String, value: String, context: Context?) {
        if (context == null)
            return
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.commit()
    }
    fun getPref(key: String, context: Context): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, null)
    }

    fun deleteSharedData(context: Context?) {
        if (context == null)
            return
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
    }
}
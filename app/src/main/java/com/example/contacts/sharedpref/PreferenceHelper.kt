package com.example.contacts.sharedpref

import android.content.Context
import android.content.SharedPreferences


class PreferenceHelper {

    private val SHARED_PREFERENCE_KEY = "com.example.contacts"

    fun getSharedPreference(context: Context): SharedPreferences? {
        return context.getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    fun writeDBStoreStatusToPreference(context: Context?, key: String?, value: Boolean) {
        val editor = getSharedPreference(context!!)!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getDBStoreStatusFromPreference(context: Context?, key: String?): Boolean {
        return getSharedPreference(context!!)!!.getBoolean(key, false)
    }

}
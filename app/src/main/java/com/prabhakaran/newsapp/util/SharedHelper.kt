package com.prabhakaran.newsapp.util

import android.content.Context
import android.content.SharedPreferences

class SharedHelper(context: Context) {
    private var preferences: SharedPreferences? = null

    init {
        if (preferences == null)
            preferences = context.getSharedPreferences("news", Context.MODE_PRIVATE)
    }

    fun setString(key: String, value: String) {
        preferences?.edit()?.putString(key, value)?.apply()
    }

    fun setBoolean(key: String, value: Boolean) {
        preferences?.edit()?.putBoolean(key, value)?.apply()
    }

    fun setInt(key: String, value: Int) {
        preferences?.edit()?.putInt(key, value)?.apply()
    }

    fun getString(key: String): String? {
        return preferences?.getString(key, null)
    }

    fun getBoolean(key: String): Boolean {
        return preferences?.getBoolean(key, false) ?: false
    }

    fun getInt(key: String): Int {
        return preferences?.getInt(key, -1) ?: -1
    }

    fun sessionClear() {
        preferences!!.edit().clear().apply()
    }


    fun remove(key: String) {
        preferences!!.edit().remove(key).apply()
    }
}
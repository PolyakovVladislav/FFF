package com.fairyfo.frenzy.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPrefs(context: Context) {

    private val preferences = context.getSharedPreferences("Default", Context.MODE_PRIVATE)

    var signInOption by PreferenceFieldDelegate.Int("musicLevel", 2)

    var isUserSignedIn by PreferenceFieldDelegate.Boolean("isUserSignedIn")

    var soundLevel by PreferenceFieldDelegate.Int("musicLevel", 50)

    var vibratingLevel by PreferenceFieldDelegate.Int("vibratingLevel", 50)

    var balance by PreferenceFieldDelegate.Long("balance", 5000L)

    var lastBetGame1 by PreferenceFieldDelegate.Long("lastBetGame1", 500L)

    var lastBetGame2 by PreferenceFieldDelegate.Long("lastBetGame2", 500L)

    var lastBetGame3 by PreferenceFieldDelegate.Long("lastBetGame3", 500L)

    var lastWinGame1 by PreferenceFieldDelegate.Long("lastWinGame1", 0L)

    var lastWinGame2 by PreferenceFieldDelegate.Long("lastWinGame2", 0L)

    var lastWinGame3 by PreferenceFieldDelegate.Long("lastWinGame3", 0L)

    private sealed class PreferenceFieldDelegate<T>(protected val key: kotlin.String) :
        ReadWriteProperty<SharedPrefs, T> {

        class Boolean(key: kotlin.String) : PreferenceFieldDelegate<kotlin.Boolean>(key) {

            override fun getValue(thisRef: SharedPrefs, property: KProperty<*>) =
                thisRef.preferences.getBoolean(key, false)

            override fun setValue(
                thisRef: SharedPrefs,
                property: KProperty<*>,
                value: kotlin.Boolean,
            ) = thisRef.preferences.edit().putBoolean(key, value).apply()
        }

        class Int(
            key: kotlin.String,
            private val defaultValue: kotlin.Int
        ) : PreferenceFieldDelegate<kotlin.Int>(key) {

            override fun getValue(thisRef: SharedPrefs, property: KProperty<*>) =
                thisRef.preferences.getInt(key, defaultValue)

            override fun setValue(thisRef: SharedPrefs, property: KProperty<*>, value: kotlin.Int) =
                thisRef.preferences.edit().putInt(key, value).apply()
        }

        class Long(
            key: kotlin.String,
            private val defaultValue: kotlin.Long,
        ) : PreferenceFieldDelegate<kotlin.Long>(key) {

            override fun getValue(thisRef: SharedPrefs, property: KProperty<*>) =
                thisRef.preferences.getLong(key, defaultValue)

            override fun setValue(
                thisRef: SharedPrefs,
                property: KProperty<*>,
                value: kotlin.Long,
            ) = thisRef.preferences.edit().putLong(key, value).apply()
        }

        class String(key: kotlin.String) : PreferenceFieldDelegate<kotlin.String>(key) {

            override fun getValue(thisRef: SharedPrefs, property: KProperty<*>) =
                thisRef.preferences.getString(key, "") ?: ""

            override fun setValue(
                thisRef: SharedPrefs,
                property: KProperty<*>,
                value: kotlin.String,
            ) = thisRef.preferences.edit().putString(key, value).apply()
        }

        class IntSet(key: kotlin.String) :
            PreferenceFieldDelegate<Set<kotlin.Int>>(key) {

            override fun getValue(
                thisRef: SharedPrefs,
                property: KProperty<*>,
            ): Set<kotlin.Int> {
                val stringSet = thisRef.preferences.getStringSet(key, setOf()) ?: setOf()
                return stringSet.map { string ->
                    try {
                        string.toInt()
                    } catch (e: NumberFormatException) {
                        Log.e("tag", e.message.toString())
                        -1
                    }
                }.toSet()
            }

            override fun setValue(
                thisRef: SharedPrefs,
                property: KProperty<*>,
                value: Set<kotlin.Int>,
            ) = thisRef.preferences.edit().putStringSet(key, value.map { it.toString() }.toSet())
                .apply()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SharedPrefs? = null

        fun getInstance(activity: Activity): SharedPrefs =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefs(activity).also { INSTANCE = it }
            }
    }
}

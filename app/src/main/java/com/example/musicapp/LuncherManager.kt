package com.example.musicapp

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor


class LuncherManager(context: Context) {
    var sharedPreferences: SharedPreferences
    var editor: Editor
    fun setFirstLunch(isFirst: Boolean) {
        editor.putBoolean(IS_FIRST_TIME, isFirst)
        editor.commit()
    }

    val isFirstTime: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_TIME, true)

    companion object {
        private const val PREF_NAME = "LunchManger"
        private const val IS_FIRST_TIME = "isFirst"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0)
        editor = sharedPreferences.edit()
    }
}

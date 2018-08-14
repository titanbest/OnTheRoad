package com.sergey.data.repository

import android.content.Context
import android.content.SharedPreferences
//import com.kitco.data.R
//import com.kitco.domain.model.*
//import com.kitco.domain.model.WidgetNewsInstructions.SHOW_PROGRESS
//import com.kitco.domain.repository.SettingsRepository
import com.sergey.domain.repository.SettingsRepository
import javax.inject.Inject

class SettingsStorage @Inject constructor(
        private val sharedPrefs: SharedPreferences,
        c: Context
) : SettingsRepository {

//    private companion object {
//        const val KEY_CURRENCY = "KEY_CURRENCY"
//        const val KEY_AUTO_UPDATE = "KEY_AUTO_UPDATE"
//    }
//
//    @SuppressLint("ApplySharedPref")
//    private fun SharedPreferences.inTransaction(block: SharedPreferences.Editor.() -> SharedPreferences.Editor) {
//        edit().block().commit()
//    }
//
//    private fun SharedPreferences.inAsyncTransaction(block: SharedPreferences.Editor.() -> SharedPreferences.Editor) {
//        edit().block().apply()
//    }
//
//    override fun getCurrency(): Single<String> = Single.fromCallable {
//        sharedPrefs.getString(KEY_CURRENCY, "USD")
//    }
//
//    override fun saveCurrency(currency: String): Completable = Completable.fromAction {
//        sharedPrefs.inTransaction {
//            putString(KEY_CURRENCY, currency)
//        }
//    }
//
//    override fun getAutoUpdate(): Boolean = sharedPrefs.getBoolean(KEY_AUTO_UPDATE, true)
//
//    override fun saveAutoUpdate(state: Boolean) = sharedPrefs.inAsyncTransaction {
//        putBoolean(KEY_AUTO_UPDATE, state)
//    }
}
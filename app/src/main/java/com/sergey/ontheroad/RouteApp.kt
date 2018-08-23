package com.sergey.ontheroad

import android.content.Context
import com.sergey.ontheroad.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class RouteApp : DaggerApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().create(this)
    }
}
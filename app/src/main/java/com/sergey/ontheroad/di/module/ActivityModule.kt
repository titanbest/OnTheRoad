@file:Suppress("unused")

package com.sergey.ontheroad.di.module

import com.sergey.ontheroad.di.annotation.PerActivity
import com.sergey.ontheroad.view.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Module(includes = [(AndroidSupportInjectionModule::class), (FragmentModule::class)])
interface ActivityModule {

    @ContributesAndroidInjector
    @PerActivity
    fun mainActivity(): MainActivity
}
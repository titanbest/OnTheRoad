package com.sergey.ontheroad.di.module

import android.content.Context
import com.sergey.ontheroad.di.annotation.PerApplication
import com.sergey.ontheroad.RouteApp
import dagger.Module
import dagger.Provides

@Module(includes = [(PresenterModule::class)])
class ApplicationModule {

    @Provides
    @PerApplication
    fun provideApplicationContext(application: RouteApp): Context = application
}
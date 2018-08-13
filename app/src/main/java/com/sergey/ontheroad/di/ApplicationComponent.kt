package com.sergey.ontheroad.di

import com.sergey.ontheroad.di.annotation.PerApplication
import com.sergey.ontheroad.RouteApp
import com.sergey.ontheroad.di.module.CoreModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

/**
 * A component whose lifetime is the life of the application.
 */
@PerApplication
@Component(modules = [(CoreModule::class), (AndroidSupportInjectionModule::class)])
interface ApplicationComponent : AndroidInjector<RouteApp> {

    @Component.Builder abstract class Builder : AndroidInjector.Builder<RouteApp>()
}
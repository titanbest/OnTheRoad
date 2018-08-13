package com.sergey.ontheroad.di.module;

import com.sergey.ontheroad.di.module.viewmodel.ViewModelModule
import dagger.Module

@Module(includes = [
    (ApplicationModule::class),
    (ViewModelModule::class),
    (ActivityModule::class),
    (FragmentModule::class)
])
interface CoreModule
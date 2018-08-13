package com.sergey.ontheroad.di.module

import com.sergey.ontheroad.di.annotation.PerFragment
import com.sergey.ontheroad.view.fragments.MapsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {

    @ContributesAndroidInjector
    @PerFragment
    fun mapsFragment(): MapsFragment
}
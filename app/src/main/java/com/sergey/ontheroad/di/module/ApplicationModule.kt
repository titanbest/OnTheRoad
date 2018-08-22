package com.sergey.ontheroad.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sergey.data.repository.ServerApi
import com.sergey.data.repository.ServerApi.Companion.BASE_URL
import com.sergey.data.repository.ServerStorage
import com.sergey.domain.repository.ServerRepository
import com.sergey.ontheroad.di.annotation.PerApplication
import com.sergey.ontheroad.RouteApp
import com.titanium.data.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [(PresenterModule::class)])
class ApplicationModule {

    @Provides
    @PerApplication
    fun provideApplicationContext(application: RouteApp): Context = application

    @Provides
    @PerApplication
    fun provideGson(): Gson {
        return GsonBuilder()
                .setLenient()
                .create()
    }

    @Provides
    @PerApplication
    fun okHttpClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @PerApplication
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        val baseUrl = BASE_URL
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @PerApplication
    fun provideServerApi(retrofit: Retrofit): ServerApi {
        return retrofit.create(ServerApi::class.java)
    }

    @Provides
    @PerApplication
    fun provideApiRepository(storage: ServerStorage): ServerRepository = storage
}
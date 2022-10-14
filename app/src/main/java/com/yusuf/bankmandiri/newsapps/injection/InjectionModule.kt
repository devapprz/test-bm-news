package com.yusuf.bankmandiri.newsapps.injection

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object InjectionModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setPrettyPrinting().create()

}
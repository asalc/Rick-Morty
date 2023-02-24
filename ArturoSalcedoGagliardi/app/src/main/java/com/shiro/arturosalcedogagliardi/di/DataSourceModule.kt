package com.shiro.arturosalcedogagliardi.di

import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.local.database.CharactersDao
import com.shiro.arturosalcedogagliardi.data.source.local.impl.CharactersLocalDataSourceImpl
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.api.CharactersApi
import com.shiro.arturosalcedogagliardi.data.source.remote.impl.CharactersRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteCharactersDataSource(
        charactersApi: CharactersApi
    ): CharactersRemoteDataSource =
        CharactersRemoteDataSourceImpl(charactersApi)

    @Provides
    @Singleton
    fun provideLocalCharacterDataSource(
        charactersDao: CharactersDao
    ): CharactersLocalDataSource =
        CharactersLocalDataSourceImpl(charactersDao)

}
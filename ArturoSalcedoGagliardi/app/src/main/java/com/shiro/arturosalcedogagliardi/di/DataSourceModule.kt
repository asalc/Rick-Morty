package com.shiro.arturosalcedogagliardi.di

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

}
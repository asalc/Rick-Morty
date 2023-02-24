package com.shiro.arturosalcedogagliardi.di

import com.shiro.arturosalcedogagliardi.data.repository.CharactersRepositoryImpl
import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCharactersRepository(
        charactersRemoteDataSource: CharactersRemoteDataSource,
        charactersLocalDataSource: CharactersLocalDataSource
    ): CharactersRepository =
        CharactersRepositoryImpl(
            charactersLocalDataSource,
            charactersRemoteDataSource
        )

}
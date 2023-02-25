package com.shiro.arturosalcedogagliardi.di

import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetAllCharactersUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetCharacterDetailsUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.UpdateCharacterUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.GetAllCharactersUseCaseImpl
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.GetCharacterDetailsUseCaseImpl
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.UpdateCharacterUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetAllCharactersUseCase(
        charactersRepository: CharactersRepository
    ): GetAllCharactersUseCase =
        GetAllCharactersUseCaseImpl(charactersRepository)

    @Provides
    @Singleton
    fun providesGetCharacterDetailsUseCase(
        charactersRepository: CharactersRepository
    ): GetCharacterDetailsUseCase =
        GetCharacterDetailsUseCaseImpl(charactersRepository)

    @Provides
    @Singleton
    fun providesUpdateCharacterUseCase(
        charactersRepository: CharactersRepository
    ): UpdateCharacterUseCase =
        UpdateCharacterUseCaseImpl(charactersRepository)

}
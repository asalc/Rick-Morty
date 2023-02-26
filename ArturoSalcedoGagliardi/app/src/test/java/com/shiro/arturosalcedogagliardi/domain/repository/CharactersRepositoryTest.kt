package com.shiro.arturosalcedogagliardi.domain.repository

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.mappers.toDomain
import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import com.shiro.arturosalcedogagliardi.data.repository.CharactersRepositoryImpl
import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult
import com.shiro.arturosalcedogagliardi.domain.model.Pager
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersRepositoryTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var charactersLocalDataSource: CharactersLocalDataSource

    @RelaxedMockK
    private lateinit var charactersRemoteDataSource: CharactersRemoteDataSource

    private lateinit var charactersRepository: CharactersRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        charactersRepository = CharactersRepositoryImpl(
            charactersLocalDataSource,
            charactersRemoteDataSource
        )
    }

    @Test
    fun `When a characters list is requested, it is successfully retrieved from the data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRemoteDataSource.getAllCharacters(any()) } returns getMockResultRemote()
            coEvery { charactersLocalDataSource.getAllCharacters() } returns getMockCharactersList()

            //WHEN
            val response = charactersRepository.getAllCharacters(1)

            //THEN
            response.onSuccess { result ->
                getMockCharactersList().toDomain().forEach { character ->
                    assert(result?.results?.find { it.id == character.id } != null)
                }
            }
        }
    }

    private fun getMockResultRemote(): Result<CharacterResultRemote> {
        val characters = arrayListOf<CharacterRemote>()
        repeat(20) {
            characters.add(CharacterRemote(id = it))
        }
        return Result.success(
            CharacterResultRemote(
                info = Pager(),
                results = characters
            )
        )
    }

    private fun getMockCharactersList(): List<CharacterLocal> {
        val characters = arrayListOf<CharacterLocal>()
        repeat(20) {
            characters.add(CharacterLocal(id = it))
        }
        return characters
    }
}
package com.shiro.arturosalcedogagliardi.domain.repository

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.mappers.toDomain
import com.shiro.arturosalcedogagliardi.data.mappers.toLocal
import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import com.shiro.arturosalcedogagliardi.data.repository.CharactersRepositoryImpl
import com.shiro.arturosalcedogagliardi.data.source.local.CharactersLocalDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.CharactersRemoteDataSource
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
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

    @Test
    fun `When a characters list is requested, an exception is thrown from the remote data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRemoteDataSource.getAllCharacters(any()) } returns getExceptionMockResult()

            //WHEN
            val response = charactersRepository.getAllCharacters(1)

            //THEN
            response.onFailure {
                assert(it is Exception)
            }
        }
    }

    @Test
    fun `When a characters list is requested for the first time, and an error occurs on the remote data source, an empty list is retrieved from the local data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRemoteDataSource.getAllCharacters(any()) } returns getFailureMockResult()
            coEvery { charactersLocalDataSource.getAllCharacters() } returns listOf()

            //WHEN
            val response = charactersRepository.getAllCharacters(1)

            //THEN
            response.onSuccess { result ->
                assert(result?.results?.isEmpty() == true)
            }
        }
    }

    @Test
    fun `When a character's details are requested, they are retrieved successfully`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRemoteDataSource.getCharacterDetails(any()) } returns getMockCharacterResult()
            coEvery { charactersLocalDataSource.getCharacterDetails(any()) } returns getMockCharacter().toLocal()

            //WHEN
            val response = charactersRepository.getCharacterDetails(getMockCharacter().id ?: 0)

            //THEN
            response.onSuccess {
                assert(it?.id == getMockCharacter().id)
            }
        }
    }

    @Test
    fun `When a character's details are requested, an exception is thrown from the remote data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRemoteDataSource.getCharacterDetails(any()) } returns getExceptionMockResult()

            //WHEN
            val response = charactersRepository.getCharacterDetails(getMockCharacter().id ?: 0)

            //THEN
            response.onFailure {
                assert(it is Exception)
            }
        }
    }

    @Test
    fun `When a character's details are requested for the first time, and an error occurs on the remote data source, null is retrieved from the local data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRemoteDataSource.getCharacterDetails(any()) } returns getFailureMockResult()
            coEvery { charactersLocalDataSource.getCharacterDetails(any()) } returns null

            //WHEN
            val response = charactersRepository.getCharacterDetails(getMockCharacter().id ?: 0)

            //THEN
            response.onSuccess {
                assert(it == null)
            }
        }
    }

    @Test
    fun `When the user updates a character's data, the operation completes successfully`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersLocalDataSource.updateCharacterFromDomain(any()) } returns Result.success(true)

            //WHEN
            val response = charactersRepository.updateCharacter(getMockCharacter().toDomain())

            //THEN
            response.onSuccess {
                assert(it)
            }
        }
    }

    @Test
    fun `When the user updates a character's data, the operation doesn't complete`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersLocalDataSource.updateCharacterFromDomain(any()) } returns Result.success(false)

            //WHEN
            val response = charactersRepository.updateCharacter(getMockCharacter().toDomain())

            //THEN
            response.onSuccess {
                assert(!it)
            }
        }
    }

    @Test
    fun `When the user updates a character's data, an exception is thrown`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersLocalDataSource.updateCharacterFromDomain(any()) } returns getExceptionMockResult()

            //WHEN
            val response = charactersRepository.updateCharacter(getMockCharacter().toDomain())

            //THEN
            response.onFailure {
                assert(it is Exception)
            }
        }
    }

    @Test
    fun `When the user deletes a character, the operation completes successfully`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersLocalDataSource.deleteCharacter(any()) } returns Result.success(true)

            //WHEN
            val response = charactersRepository.deleteCharacter(getMockCharacter().toDomain())

            //THEN
            response.onSuccess {
                assert(it == getMockCharacter().id)
            }
        }
    }

    @Test
    fun `When the user deletes character, the operation doesn't complete`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersLocalDataSource.deleteCharacter(any()) } returns Result.success(false)

            //WHEN
            val response = charactersRepository.deleteCharacter(getMockCharacter().toDomain())

            //THEN
            response.onFailure {
                assert(it is ApiError.Unknown)
            }
        }
    }

    @Test
    fun `When the user deletes a character, an exception is thrown`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersLocalDataSource.deleteCharacter(any()) } returns getExceptionMockResult()

            //WHEN
            val response = charactersRepository.deleteCharacter(getMockCharacter().toDomain())

            //THEN
            response.onFailure {
                assert(it is Exception)
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

    private fun getMockCharacter() = CharacterRemote(id = 3)
    private fun getMockCharacterResult() = Result.success(getMockCharacter())

    private fun <T> getFailureMockResult() = Result.failure<T>(ApiError.Unknown())
    private fun <T> getExceptionMockResult() = Result.failure<T>(Exception())
}
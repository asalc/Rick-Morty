package com.shiro.arturosalcedogagliardi.data.source.remote

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.model.CharacterResultRemote
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterRemote
import com.shiro.arturosalcedogagliardi.data.source.remote.api.CharactersApi
import com.shiro.arturosalcedogagliardi.data.source.remote.impl.CharactersRemoteDataSourceImpl
import com.shiro.arturosalcedogagliardi.domain.model.Pager
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersRemoteDataSourceTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var charactersApi: CharactersApi

    private lateinit var charactersRemoteDataSource: CharactersRemoteDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        charactersRemoteDataSource = CharactersRemoteDataSourceImpl(charactersApi)
    }

    @Test
    fun `When a characters list is requested, it is retrieved successfully from the data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersApi.getCharacters(any()) } returns getMockResponse()

            //WHEN
            val response = charactersRemoteDataSource.getAllCharacters(1)

            //THEN
            response.onSuccess {
                assert(!it?.results.isNullOrEmpty())
            }
        }
    }

    @Test
    fun `When a characters list is requested, an error occurs`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersApi.getCharacters(any()) } returns getMockFailureResponse()

            //WHEN
            val response = charactersRemoteDataSource.getAllCharacters(1)

            //THEN
            response.onSuccess {
                assert(it == null)
            }
        }
    }

    @Test
    fun `When a character's details are requested, they are retrieved successfully from the data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersApi.getCharacterDetails(any()) } returns getMockCharacterResponse()

            //WHEN
            val response = charactersRemoteDataSource.getCharacterDetails(getMockCharacter().id ?: 0)

            //THEN
            response.onSuccess {
                assert(it?.id == getMockCharacter().id)
            }
        }
    }

    @Test
    fun `When a character's details are requested, an error occurs`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersApi.getCharacters(any()) } returns getMockFailureResponse()

            //WHEN
            val response = charactersRemoteDataSource.getCharacterDetails(getMockCharacter().id ?: 0)

            //THEN
            response.onSuccess {
                assert(it == null)
            }
        }
    }

    private fun getMockResponse(): Response<CharacterResultRemote> {
        val characters = arrayListOf<CharacterRemote>()
        repeat(20) {
            characters.add(CharacterRemote(id = it))
        }
        return Response.success(
            CharacterResultRemote(
                info = Pager(),
                results = characters
            )
        )
    }

    private fun getMockCharacter() = CharacterRemote(id = 3)
    private fun getMockCharacterResponse() =
        Response.success(getMockCharacter())

    private fun <T> getMockFailureResponse() = Response.error<T>(404, "".toResponseBody())
}


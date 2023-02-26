package com.shiro.arturosalcedogagliardi.data.source.local

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.model.character.CharacterLocal
import com.shiro.arturosalcedogagliardi.data.source.local.database.CharactersDao
import com.shiro.arturosalcedogagliardi.data.source.local.impl.CharactersLocalDataSourceImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersLocalDataSourceTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var charactersDao: CharactersDao

    private lateinit var charactersLocalDataSource: CharactersLocalDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        charactersLocalDataSource = CharactersLocalDataSourceImpl(charactersDao)
    }

    @Test
    fun `When a characters list is requested, it is retrieved successfully from the data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersDao.getAllCharacters() } returns getMockResult()

            //WHEN
            val response = charactersLocalDataSource.getAllCharacters()

            //THEN
            assert(response.isNotEmpty())
        }
    }

    @Test
    fun `When a characters list is requested, an empty list is retrieved`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersDao.getAllCharacters() } returns listOf()

            //WHEN
            val response = charactersLocalDataSource.getAllCharacters()

            //THEN
            assert(response.isEmpty())
        }
    }

    @Test
    fun `When a character's details are requested, they are retrieved successfully from the data source`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersDao.getCharacterDetails(any()) } returns getMockCharacter()

            //WHEN
            val response = charactersLocalDataSource.getCharacterDetails(getMockCharacter().id ?: 0)

            //THEN
            assert(response?.id == getMockCharacter().id)
        }
    }

    @Test
    fun `When a character's details are requested, the character is not found, so null is returned`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersDao.getCharacterDetails(any()) } returns null

            //WHEN
            val response = charactersLocalDataSource.getCharacterDetails(getMockCharacter().id ?: 0)

            //THEN
            assert(response == null)
        }
    }

    private fun getMockResult(): List<CharacterLocal> {
        val characters = mutableListOf<CharacterLocal>()
        repeat(20) {
            characters.add(CharacterLocal())
        }
        return characters
    }

    private fun getMockCharacter() = CharacterLocal(id = 5)
}
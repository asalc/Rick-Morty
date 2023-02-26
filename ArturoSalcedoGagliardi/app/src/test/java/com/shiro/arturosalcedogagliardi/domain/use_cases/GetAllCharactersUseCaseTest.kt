package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult
import com.shiro.arturosalcedogagliardi.domain.model.Pager
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.GetAllCharactersUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllCharactersUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var getAllCharactersUseCase: GetAllCharactersUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getAllCharactersUseCase = GetAllCharactersUseCaseImpl(charactersRepository)
    }

    @Test
    fun `When the use case is invoked, a non-empty list is returned from the repository`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.getAllCharacters(any()) } returns getSuccessMockResult()

            //WHEN
            val result = getAllCharactersUseCase(1)

            //THEN
            result.onSuccess {
                assert(!it?.results.isNullOrEmpty())
            }
        }
    }

    @Test
    fun `When the use case is invoked, an error occurs, so the repository throws an exception`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.getAllCharacters(any()) } returns getFailureMockResult()

            //WHEN
            val result = getAllCharactersUseCase(1)

            //THEN
            result.onFailure {
                assert(it is ApiError.Unknown)
            }
        }
    }

    private fun getSuccessMockResult(): Result<CharacterResult> {
        val characters = arrayListOf<Character>()
        repeat(5) { characters.add(Character()) }
        return Result.success(
            CharacterResult(
                info = Pager(),
                results = characters
            )
        )
    }

    private fun <T> getFailureMockResult() = Result.failure<T>(ApiError.Unknown())
}
package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.GetCharacterDetailsUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCharacterDetailsUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCharacterDetailsUseCase = GetCharacterDetailsUseCaseImpl(charactersRepository)
    }

    @Test
    fun `When the use case is invoked, the correct character is retrieved`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.getCharacterDetails(any()) } returns getSuccessMockCharacterResult()

            //WHEN
            val response = getCharacterDetailsUseCase(3)

            //THEN
            response.onSuccess {
                assert(it?.id == getSuccessMockCharacterResult().getOrNull()?.id)
            }
        }
    }

    @Test
    fun `When the use case is invoked, an exception is thrown`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.getCharacterDetails(any()) } returns getFailureMockCharacterResult()

            //WHEN
            val response = getCharacterDetailsUseCase(3)

            //THEN
            response.onFailure {
                assert(it is ApiError.Unknown)
            }
        }
    }

    private fun getSuccessMockCharacterResult() = Result.success(Character(id = 3))
    private fun getFailureMockCharacterResult() = Result.failure<Character>(ApiError.Unknown())
}
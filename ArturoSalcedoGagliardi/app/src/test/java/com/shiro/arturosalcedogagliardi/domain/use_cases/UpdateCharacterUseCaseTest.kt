package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.UpdateCharacterUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateCharacterUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var updateCharacterUseCase: UpdateCharacterUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateCharacterUseCase = UpdateCharacterUseCaseImpl(charactersRepository)
    }

    @Test
    fun `When the use case is invoked, the character's data is updated successfully`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.updateCharacter(any()) } returns getSuccessMockResult()

            //WHEN
            val response = updateCharacterUseCase(getMockCharacter())

            //THEN
            response.onSuccess {
                assert(it)
            }
        }
    }

    @Test
    fun `When the use case is invoked, an exception is thrown`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.updateCharacter(any()) } returns getFailureMockResult()

            //WHEN
            val response = updateCharacterUseCase(getMockCharacter())

            //THEN
            response.onFailure {
                assert(it is ApiError.Unknown)
            }
        }
    }

    private fun getSuccessMockResult() = Result.success(true)
    private fun getFailureMockResult() = Result.failure<Boolean>(ApiError.Unknown())

    private fun getMockCharacter() = Character()
}
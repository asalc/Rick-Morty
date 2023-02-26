package com.shiro.arturosalcedogagliardi.domain.use_cases

import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.repository.CharactersRepository
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.DeleteCharacterUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteCharacterUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var charactersRepository: CharactersRepository

    private lateinit var deleteCharacterUseCase: DeleteCharacterUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        deleteCharacterUseCase = DeleteCharacterUseCaseImpl(charactersRepository)
    }

    @Test
    fun `When the use case is invoked, the character's data is updated successfully`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.deleteCharacter(any()) } returns getSuccessMockResult()

            //WHEN
            val response = deleteCharacterUseCase(getMockCharacter())

            //THEN
            response.onSuccess {
                assert(it == getMockCharacter().id)
            }
        }
    }

    @Test
    fun `When the use case is invoked, an exception is thrown`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { charactersRepository.deleteCharacter(any()) } returns getFailureMockResult()

            //WHEN
            val response = deleteCharacterUseCase(getMockCharacter())

            //THEN
            response.onFailure {
                assert(it is ApiError.Unknown)
            }
        }
    }

    private fun getSuccessMockResult() = Result.success(3)
    private fun getFailureMockResult() = Result.failure<Int>(ApiError.Unknown())

    private fun getMockCharacter() = Character(id = 3)
}
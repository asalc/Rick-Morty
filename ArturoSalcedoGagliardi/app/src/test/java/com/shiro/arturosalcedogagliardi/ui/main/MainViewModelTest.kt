package com.shiro.arturosalcedogagliardi.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.testing.TestLifecycleOwner
import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.model.Character
import com.shiro.arturosalcedogagliardi.domain.model.CharacterResult
import com.shiro.arturosalcedogagliardi.domain.model.Pager
import com.shiro.arturosalcedogagliardi.domain.use_cases.DeleteCharacterUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.GetAllCharactersUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.DeleteCharacterUseCaseImpl
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.GetAllCharactersUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getAllCharactersUseCase: GetAllCharactersUseCaseImpl

    @MockK
    private lateinit var deleteCharacterUseCase: DeleteCharacterUseCaseImpl

    @InjectMockKs
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `When a characters list is requested, the results are retrieved successfully`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { getAllCharactersUseCase(any()) } returns getSuccessMockResult()

            //WHEN
            mainViewModel.getCharacters()

            //THEN
            mainViewModel.charactersList.observe(TestLifecycleOwner()) {
                assert(!it.isNullOrEmpty())
            }
        }
    }

    @Test
    fun `When a characters list is requested, an exception is thrown`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { getAllCharactersUseCase(any()) } returns getFailureMockResult()

            //WHEN
            mainViewModel.getCharacters()

            //THEN
            mainViewModel.errorResourceId.observe(TestLifecycleOwner()) {
                assert(it == ApiError.Unknown().errorMessage)
            }
        }
    }

    @Test
    fun `When the user deletes a character, the operation completes successfully`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { deleteCharacterUseCase(any()) } returns getMockCharacterId()

            //WHEN
            mainViewModel.deleteCharacter(getMockCharacter())

            //THEN
            mainViewModel.deletedCharacterId.observe(TestLifecycleOwner()) {
                assert(it == getMockCharacterId().getOrNull())
            }
        }
    }

    @Test
    fun `When the user deletes a character, the operation doesn't complete`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { deleteCharacterUseCase(any()) } returns getFailureMockResult()

            //WHEN
            mainViewModel.deleteCharacter(getMockCharacter())

            //THEN
            mainViewModel.errorResourceId.observe(TestLifecycleOwner()) {
                assert(it == ApiError.Unknown().errorMessage)
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

    private fun getMockCharacter() = Character(id = 3)
    private fun getMockCharacterId() = Result.success(getMockCharacter().id)

    private fun <T> getFailureMockResult() = Result.failure<T>(ApiError.Unknown())
}
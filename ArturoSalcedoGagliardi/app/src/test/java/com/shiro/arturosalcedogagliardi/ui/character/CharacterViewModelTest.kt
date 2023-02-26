package com.shiro.arturosalcedogagliardi.ui.character

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.testing.TestLifecycleOwner
import com.shiro.arturosalcedogagliardi.TestCoroutineRule
import com.shiro.arturosalcedogagliardi.data.source.remote.api.ApiError
import com.shiro.arturosalcedogagliardi.domain.use_cases.UpdateCharacterUseCase
import com.shiro.arturosalcedogagliardi.domain.use_cases.impl.UpdateCharacterUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var updateCharacterUseCase: UpdateCharacterUseCaseImpl

    @InjectMockKs
    private lateinit var characterViewModel: CharacterViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `When the user updates a character's data, the operation is successful`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { updateCharacterUseCase(any()) } returns Result.success(true)

            //WHEN
            characterViewModel.saveChanges()

            //THEN
            characterViewModel.characterUpdateSuccessful.observe(TestLifecycleOwner()) {
                assert(it)
            }
        }
    }

    @Test
    fun `When the user updates a character's data, the operation doesn't complete`() {
        testCoroutineRule.runTest {
            //GIVEN
            coEvery { updateCharacterUseCase(any()) } returns Result.failure(Exception())

            //WHEN
            characterViewModel.saveChanges()

            //THEN
            characterViewModel.characterError.observe(TestLifecycleOwner()) {
                assert(it == ApiError.Unknown().errorMessage)
            }
        }
    }
}
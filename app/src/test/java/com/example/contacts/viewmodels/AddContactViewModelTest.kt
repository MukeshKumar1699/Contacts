package com.example.contacts.viewmodels

import com.example.contacts.MainCoroutineRule
import com.example.contacts.PhoneDetails
import com.example.contacts.repositories.AddContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.runners.MockitoJUnitRunner
import java.util.Collections.emptyList

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddContactViewModelTest {

    private lateinit var viewModel: AddContactViewModel

//    @Rule
//    @JvmField
//    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var initRule: MockitoRule = MockitoJUnit.rule()

    @Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    lateinit var repository: AddContactRepository

    @Before
    fun setup() {
        viewModel = AddContactViewModel(repository)
    }

    @Test
    fun getPhoneNumberTest() {

            doReturn(emptyList<PhoneDetails>()).
                    `when`(repository)
                .getPhoneNumbers("")

            val value = viewModel.fetchPhoneNumber("")
            print(value)
            assertTrue(true)
    }

    @Test
    fun sampleTest() {

        assertEquals(2, viewModel.sample())
    }

}
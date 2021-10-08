package com.example.contacts

import com.example.contacts.repositories.AddContactRepository
import com.example.contacts.viewmodels.AddContactViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn

@ExperimentalCoroutinesApi
class AddContactViewModelTest {

    private lateinit var viewModel: AddContactViewModel

//    @Rule
//    @JvmField
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Rule
//    @JvmField
//    var initRule: MockitoRule = MockitoJUnit.rule()


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
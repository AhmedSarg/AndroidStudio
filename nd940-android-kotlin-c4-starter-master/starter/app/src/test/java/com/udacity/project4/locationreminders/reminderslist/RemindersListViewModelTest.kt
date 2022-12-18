package com.udacity.project4.locationreminders.reminderslist

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.core.inject
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest : KoinComponent {

    //TODO: provide testing to the RemindersListViewModel and its live data objects

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val remindersListViewModel: RemindersListViewModel by inject()

    private lateinit var remindersSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        remindersSource = FakeDataSource()
        val reminder1 = ReminderDTO(
            "title1",
            "description1", "cafe1",
            31.0, 31.0
        )
        val reminder2 = ReminderDTO(
            "title2",
            "description2", "cafe2",
            32.0, 32.0
        )
        val reminder3 = ReminderDTO(
            "title3",
            "description3", "cafe3",
            33.0, 33.0
        )
        remindersSource.addReminders(reminder1, reminder2, reminder3)
        loadKoinModules(module {
            single(override = true) {
                RemindersListViewModel(ApplicationProvider.getApplicationContext(), remindersSource)
            }
        })
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun showLoading_loadBeforeAddAndEndAfterComplete() = runBlockingTest {
        mainCoroutineRule.pauseDispatcher()
        remindersListViewModel.loadReminders()
        assertThat(remindersListViewModel.showLoading.value, `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(remindersListViewModel.showLoading.value, `is`(false))
    }

    @Test
    fun showSnackBar_returnTestException() = runBlockingTest {
        remindersSource.setShouldReturnError()
        remindersListViewModel.loadReminders()
        val result = remindersSource.getReminders() as Result.Error
        assertThat(remindersListViewModel.showSnackBar.value, `is`(result.message))
    }

    @Test
    fun showNoData_returnNoData() {
        remindersSource.setShouldReturnError()
        remindersListViewModel.loadReminders()
        assertThat(remindersListViewModel.showNoData.value, `is`(true))
    }

}
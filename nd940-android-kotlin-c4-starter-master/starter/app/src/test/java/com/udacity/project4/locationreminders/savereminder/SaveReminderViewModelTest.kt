package com.udacity.project4.locationreminders.savereminder


import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.events.Event
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.FakeTestRepository
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.utils.SingleLiveEvent

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    //TODO: provide testing to the SaveReminderView and its live data objects

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var saveReminderViewModel : SaveReminderViewModel

    //private lateinit var remindersRepository : FakeTestRepository
    private lateinit var remindersSource : FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        remindersSource = FakeDataSource()
        val reminder1 = ReminderDTO(
            "title1",
            "description1"
            ,"cafe1",
            31.0, 31.0
        )
        val reminder2 = ReminderDTO(
            "title2",
            "description2"
            ,"cafe2",
            32.0, 32.0
        )
        val reminder3 = ReminderDTO(
            "title3",
            "description3"
            ,"cafe3",
            33.0, 33.0
        )
        remindersSource.addReminders(reminder1, reminder2, reminder3)
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersSource)
    }

    @Test
    fun onCLear() {
        saveReminderViewModel.saveReminder(ReminderDataItem(
            "title0",
            "description0",
            "cafe0",
            0.0, 0.0
        ))
        saveReminderViewModel.onClear()
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), nullValue())
    }

    @Test
    fun saveReminderDone_endLoadingIcon() {
        saveReminderViewModel.saveReminder(ReminderDataItem(
            "title0",
            "description0",
            "cafe0",
            0.0, 0.0
        ))
        val value : Boolean = saveReminderViewModel.showLoading.getOrAwaitValue()
        assertThat(value, `is`(false))
    }

    @Test
    fun saveReminder_checkData() = runBlockingTest {
        val reminder5 = ReminderDataItem(
            "title5",
            "description5",
            "cafe5",
            5.5, 5.5
        )
        saveReminderViewModel.validateAndSaveReminder(reminder5)

        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), `is`("title5"))
        //assertThat(saveReminderViewModel.reminderDescription.getOrAwaitValue(), `is`("description5"))
        //assertThat(saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue(), `is`("cafe5"))
        //assertThat(saveReminderViewModel.latitude.getOrAwaitValue(), `is`(5.5))
        //assertThat(saveReminderViewModel.longitude.getOrAwaitValue(), `is`(5.5))
    }
}

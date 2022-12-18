package com.udacity.project4.locationreminders.reminderslist

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.inject
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest : KoinComponent {

    //TODO: provide testing to the RemindersListViewModel and its live data objects

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val RemindersListViewModel: RemindersListViewModel by inject()

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



}
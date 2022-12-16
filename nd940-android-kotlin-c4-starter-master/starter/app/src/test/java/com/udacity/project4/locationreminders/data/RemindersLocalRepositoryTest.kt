package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FakeDataSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val reminder1 = ReminderDTO(
        "title1",
        "description1"
        ,"cafe1",
        31.0, 31.0
    )
    private val reminder2 = ReminderDTO(
        "title2",
        "description2"
        ,"cafe2",
        32.0, 32.0
    )
    private val reminder3 = ReminderDTO(
        "title3",
        "description3"
        ,"cafe3",
        33.0, 33.0
    )

    private val localReminders = listOf(reminder1, reminder2, reminder3).sortedBy { it.id }
    private lateinit var dataSource : FakeDataSource

    @Before
    fun createRepository() {
        dataSource = FakeDataSource(localReminders.toMutableList())
    }

    @Test
    fun getReminders_requestsAllRemindersFromLocalDataSource() = mainCoroutineRule.runBlockingTest {
        val reminders = dataSource.getReminders() as Result.Success
        assertThat(reminders.data, IsEqual(localReminders))
    }

}
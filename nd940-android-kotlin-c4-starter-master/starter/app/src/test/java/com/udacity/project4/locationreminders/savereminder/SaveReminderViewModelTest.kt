package com.udacity.project4.locationreminders.savereminder


import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.inject
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest : KoinComponent {


    //TODO: provide testing to the SaveReminderView and its live data objects

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val saveReminderViewModel: SaveReminderViewModel by inject()

    private lateinit var remindersSource: FakeDataSource

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        Log.i("ahmed", "in setup")
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
                SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersSource)
            }
        })
        /*saveReminderViewModel =
            SaveReminderViewModel(ApplicationProvider.getApplicationContext(), remindersSource)*/
    }

    @Test
    fun onCLear_nullValues() {
        saveReminderViewModel.saveReminder(
            ReminderDataItem(
                "title0",
                "description0",
                "cafe0",
                0.0, 0.0
            )
        )
        saveReminderViewModel.onClear()
        assertThat(saveReminderViewModel.reminderTitle.getOrAwaitValue(), nullValue())
        assertThat(saveReminderViewModel.reminderDescription.getOrAwaitValue(), nullValue())
        assertThat(saveReminderViewModel.reminderSelectedLocationStr.getOrAwaitValue(), nullValue())
        assertThat(saveReminderViewModel.latitude.getOrAwaitValue(), nullValue())
        assertThat(saveReminderViewModel.longitude.getOrAwaitValue(), nullValue())
    }

    @Test
    fun saveReminderDone_endLoadingIcon() {
        mainCoroutineRule.pauseDispatcher()
        saveReminderViewModel.saveReminder(
            ReminderDataItem(
                "title0",
                "description0",
                "cafe0",
                0.0, 0.0
            )
        )
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(saveReminderViewModel.showLoading.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun saveReminder_checkData() = runBlockingTest {
        val reminder = ReminderDataItem(
            "title5",
            "description5",
            "cafe5",
            5.5, 5.5
        )
        saveReminderViewModel.validateAndSaveReminder(reminder)
        val result = remindersSource.getReminder(reminder.id)
        val act = ReminderDTO(
            reminder.title,
            reminder.description,
            reminder.location,
            reminder.latitude,
            reminder.longitude,
            reminder.id
        )
        assertThat(result.toString(), `is`(Result.Success(act).toString()))
    }

    @Test
    fun navigationCommand_navigationBack() {
        val reminder = ReminderDataItem(
            "title5",
            "description5",
            "cafe5",
            5.5, 5.5
        )
        saveReminderViewModel.saveReminder(reminder)
        val nav = saveReminderViewModel.navigationCommand.getOrAwaitValue() as NavigationCommand
        assertThat(nav, `is`(NavigationCommand::class.java))
    }

    @Test
    fun showSnackBarInt_errorMessages() {
        var reminder = ReminderDataItem(
            null,
            "description5",
            "cafe5",
            5.5, 5.5
        )
        saveReminderViewModel.validateAndSaveReminder(reminder)
        assertThat(
            saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_enter_title)
        )
        reminder = ReminderDataItem(
            "title5",
            "description5",
            null,
            5.5, 5.5
        )
        saveReminderViewModel.validateAndSaveReminder(reminder)
        assertThat(
            saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_select_location)
        )
        reminder = ReminderDataItem(
            "",
            "description5",
            "cafe5",
            5.5, 5.5
        )
        saveReminderViewModel.validateAndSaveReminder(reminder)
        assertThat(
            saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_enter_title)
        )
        reminder = ReminderDataItem(
            "title5",
            "description5",
            "",
            5.5, 5.5
        )
        saveReminderViewModel.validateAndSaveReminder(reminder)
        assertThat(
            saveReminderViewModel.showSnackBarInt.getOrAwaitValue(),
            `is`(R.string.err_select_location)
        )
    }

    @Test
    fun showToast_showReminderSavedMessage() {
        val reminder = ReminderDataItem(
            "title5",
            "description5",
            "cafe5",
            5.5, 5.5
        )
        saveReminderViewModel.saveReminder(reminder)
        val reminderSaved =
            ApplicationProvider.getApplicationContext<Context>().getString(R.string.reminder_saved)
        assertThat(saveReminderViewModel.showToast.getOrAwaitValue(), `is`(reminderSaved))
    }

}
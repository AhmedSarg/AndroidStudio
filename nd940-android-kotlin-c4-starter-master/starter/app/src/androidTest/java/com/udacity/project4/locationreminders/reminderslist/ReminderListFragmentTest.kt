package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeAndroidTestRepository
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest : AutoCloseKoinTest() {

    private lateinit var datasource: FakeAndroidTestRepository
    private lateinit var appContext: Application
    private lateinit var reminder : ReminderDTO

    @Before
    fun initRepository() {
        stopKoin()
        appContext = getApplicationContext()
        datasource = FakeAndroidTestRepository()
        val myModule = module {
            single {
                RemindersListViewModel(
                    appContext,
                    datasource
                )
            }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }

        //clear the data to start fresh
        runBlocking {
            datasource.deleteAllReminders()
        }

        reminder = ReminderDTO(
            "title",
            "description",
            "cafe",
            9.9, 9.9
        )


    }

    //    TODO: test the navigation of the fragments.
    @Test
    fun clickAddReminderButton_navigateToSaveReminderFragment() {
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.addReminderFAB)).perform(click())

        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )
    }
//    TODO: test the displayed data on the UI.

    @Test
    fun reminderListFragmentTest_displayedInUi() {

        datasource.addReminders(reminder)

        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        onView(ViewMatchers.withText("title")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("description")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText("cafe")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

//    TODO: add testing for the error messages.
    @Test
    fun reminderListFragmentTest_notDisplayedInUi() {
        launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
        onView(withId(R.id.noDataTextView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}
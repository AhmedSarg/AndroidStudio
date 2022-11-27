package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import junit.framework.TestCase.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
        //GIVEN
        val tasks = listOf<Task>(
            Task("title", "description", isCompleted = false)
        )

        //WHEN
        val result = getActiveAndCompletedStats(tasks)

        //THEN
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_both_returnsFourtySixty() {
        //GIVEN
        val tasks = listOf<Task>(
            Task("title", "description", isCompleted = true),
            Task("title", "description", isCompleted = true),
            Task("title", "description", isCompleted = false),
            Task("title", "description", isCompleted = false),
            Task("title", "description", isCompleted = false)
        )

        //WHEN
        val result = getActiveAndCompletedStats(tasks)

        //THEN
        assertThat(result.completedTasksPercent, `is`(40f))
        assertThat(result.activeTasksPercent, `is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZero() {
        //GIVEN
        val tasks = emptyList<Task>()

        //WHEN
        val result = getActiveAndCompletedStats(tasks)

        //THEN
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZero() {
        //GIVEN
        val tasks = null

        //WHEN
        val result = getActiveAndCompletedStats(tasks)

        //THEN
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
}
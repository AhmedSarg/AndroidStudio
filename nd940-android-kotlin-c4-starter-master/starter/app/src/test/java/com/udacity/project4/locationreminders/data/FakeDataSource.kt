package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(private var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

//    TODO: Create a fake data source to act as a double to the real data source

    private var shouldReturnError = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        //TODO("Return the reminders")
        if (shouldReturnError)
            return Result.Error("Test Exception")
        reminders?.let { return Result.Success(ArrayList(it)) }
        return Result.Error("Reminders Not Found")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        //TODO("save the reminder")
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        //TODO("return the reminder with the id")
        if (shouldReturnError)
            return Result.Error("Reminder Not Found")

        val reminder = reminders?.find { it.id == id }
        if (reminder != null)
            return Result.Success(reminder)
        return Result.Error("Reminder Not Found")
        /*reminders?.forEach {
            if (it.id == id)
                return Result.Success(it)
        }*/
    }

    override suspend fun deleteAllReminders() {
        //TODO("delete all the reminders")
        reminders?.clear()
    }

    fun addReminders(vararg reminderss: ReminderDTO) {
        for (reminder in reminderss) {
            reminders?.add(reminder)
        }
    }


}
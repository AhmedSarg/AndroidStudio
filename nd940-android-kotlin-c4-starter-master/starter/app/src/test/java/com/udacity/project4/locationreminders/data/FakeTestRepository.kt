package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.runBlocking


//class FakeTestRepository : RemindersRepository {
class FakeTestRepository : ReminderDataSource {

    private var shouldReturnError = false

    var remindersServiceData : LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError)
            return Result.Error("Test Exception")
        return Result.Success(remindersServiceData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        remindersServiceData[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError)
            return Result.Error("Test Exception")
        remindersServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error("Couldn't Find Reminder")
    }

    override suspend fun deleteAllReminders() {
        remindersServiceData.clear()
    }

    fun addReminders(vararg reminders: ReminderDTO) {
        for (reminder in reminders) {
            remindersServiceData[reminder.id] = reminder
        }
    }
}

package com.udacity.project4.locationreminders.data
/*

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.*

class DefaultRemindersRepository(
    private val remindersLocalRepository: ReminderDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : RemindersRepository {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        wrapEspressoIdlingResource {
            return remindersLocalRepository.getReminders()
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        wrapEspressoIdlingResource {
            coroutineScope {
                launch { remindersLocalRepository.saveReminder(reminder) }
            }
        }
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        wrapEspressoIdlingResource {
            return remindersLocalRepository.getReminder(id)
        }
    }

    override suspend fun deleteAllReminders() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                coroutineScope {
                    launch { remindersLocalRepository.deleteAllReminders() }
                }
            }
        }
    }
}*/

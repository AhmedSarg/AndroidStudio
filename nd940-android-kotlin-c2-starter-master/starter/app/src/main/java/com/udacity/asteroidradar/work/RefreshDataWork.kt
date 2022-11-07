package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getAsteroidsDatabase
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val asteroidDatabase = getAsteroidsDatabase(applicationContext)
        val repository = AsteroidsRepository(asteroidDatabase)

        return try {
            repository.refreshAsteroids(AsteroidFilter.SHOW_WEEK)
            Result.success()
        }
        catch (e: HttpException) {
            Result.retry()
        }
    }
}
package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

class AsteroidsRepository(private val asteroidDatabase: AsteroidsDatabase) {

    private val today = getToday()
    private var end = getWeek()

    fun getLiveAsteroid(today: String, end: String): LiveData<List<Asteroid>> {
        val asteroids: LiveData<List<Asteroid>> =
            Transformations.map(asteroidDatabase.asteroidDao.getAsteroids(today, end)) {
                it.asDomainModel()
            }
        return asteroids
    }

    suspend fun refreshAsteroids(filter: AsteroidFilter) {
        withContext(Dispatchers.IO) {
            try {
                end = filter.value
                val map = hashMapOf<String, String>()
                map["start_date"] = today
                map["api_key"] = "ZMEvZn9EgEC9TJGSqdfBdJayvDq2XN4HECopQtdF"
                map["end_date"] = end

                val networkData = AsteroidNetwork.asteroidService.getProperties(map)
                val obj = JSONObject(networkData)
                val data = parseAsteroidsJsonResult(obj)
                val asteroid = NetworkAsteroidContainer(data)
                asteroidDatabase.asteroidDao.clear()
                asteroidDatabase.asteroidDao.insertAll(*asteroid.asDatabaseModel())
            }
            catch (e: Exception) {

            }
        }
    }
}
package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getAsteroidsDatabase
import com.udacity.asteroidradar.network.AsteroidFilter
import com.udacity.asteroidradar.network.PictureNetwork
import com.udacity.asteroidradar.network.getToday
import com.udacity.asteroidradar.network.getWeek
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel(application: Application) : AndroidViewModel(application) {

    enum class AsteroidStatus { LOADING, ERROR, DONE }

    private var today = getToday()
    private var end = getWeek()

    private val asteroidDatabase = getAsteroidsDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(asteroidDatabase)

    private val _pic = MutableLiveData<PictureOfDay?>()
    val pic: LiveData<PictureOfDay?>
        get() = _pic

    private val _status = MutableLiveData<AsteroidStatus>()
    val status: LiveData<AsteroidStatus>
        get() = _status

    init {
        getNewAsteroids(AsteroidFilter.SHOW_WEEK)
    }

    fun getNewAsteroids(filter: AsteroidFilter) {
        viewModelScope.launch {
            end = filter.value
            _status.value = AsteroidStatus.LOADING
            asteroidsRepository.refreshAsteroids(filter)
            try {
                val map = hashMapOf<String, String>()
                map["api_key"] = "ZMEvZn9EgEC9TJGSqdfBdJayvDq2XN4HECopQtdF"
                _pic.value = PictureNetwork.pictureService?.getPictureOfDay(map)
                _status.value = AsteroidStatus.DONE
            }
            catch(e: Exception) {
                _status.value = AsteroidStatus.ERROR
                asteroids = asteroidsRepository.getLiveAsteroid(today, end)
            }
        }
    }

    var asteroids = asteroidsRepository.getLiveAsteroid(today, end)

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}
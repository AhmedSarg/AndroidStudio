package com.udacity.asteroidradar.network

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.text.SimpleDateFormat
import java.util.*

enum class AsteroidFilter(val value: String) { SHOW_WEEK(getWeek()), SHOW_TODAY(getToday()), SHOW_SAVED(getWeek()) }

interface AsteroidService {
    @GET("neo/rest/v1/feed")
    suspend fun getProperties(@QueryMap queryMap: Map<String, String>): String
}

object AsteroidNetwork {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val asteroidService = retrofit.create(AsteroidService::class.java)
}

fun getToday(): String {
    val calender = Calendar.getInstance()
    val currentTime = calender.time
    val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}

fun getWeek(): String {
    val result = getNextSevenDaysFormattedDates()
    Log.i("ahmed", result[result.lastIndex - 1])
    return result[result.lastIndex - 1]
}
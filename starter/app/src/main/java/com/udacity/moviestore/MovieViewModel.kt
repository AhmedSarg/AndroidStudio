package com.udacity.moviestore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.moviestore.models.Movie

class MovieViewModel : ViewModel() {



    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies


    private val _eventCancel = MutableLiveData<Boolean>()
    val eventCancel: LiveData<Boolean>
        get() = _eventCancel
    private val _eventDone = MutableLiveData<Boolean>()
    val eventDone: LiveData<Boolean>
        get() = _eventDone

    var nameValue = String()
    var yearValue = String()
    var directorValue = String()
    var ratingValue = String()
    init {
        _movies.value = mutableListOf()
    }

    fun addMovie() {
        _movies.value = _movies.value?.toMutableList()?.plus(Movie(nameValue, yearValue, directorValue, ratingValue))
        _eventDone.value = true
    }

    fun onCancel() {
        _eventCancel.value = true
    }
    fun onCancelComplete() {
        _eventCancel.value = false
    }
    fun onDone() {
        _eventDone.value = true
    }
    fun onDoneComplete() {
        _eventDone.value = false
        nameValue = ""
        yearValue = ""
        directorValue = ""
        ratingValue = ""
    }
}
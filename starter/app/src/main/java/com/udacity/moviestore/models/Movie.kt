package com.udacity.moviestore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(var name: String, var year: String, var director: String, var rating: String) : Parcelable
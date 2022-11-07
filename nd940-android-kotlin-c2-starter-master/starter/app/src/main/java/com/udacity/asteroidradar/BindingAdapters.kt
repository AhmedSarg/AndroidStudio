package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants.IS_HAZARD_TXT
import com.udacity.asteroidradar.Constants.NOT_HAZARD_TXT
import com.udacity.asteroidradar.main.MainViewModel.AsteroidStatus

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = IS_HAZARD_TXT
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = NOT_HAZARD_TXT
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = IS_HAZARD_TXT
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = NOT_HAZARD_TXT
    }
}

@BindingAdapter("pictureOfDay")
fun bindPictureOfDay(imageView: ImageView, pic: PictureOfDay?) {
    if (pic?.mediaType == "image") {
        Picasso.get().load(pic.url).into(imageView)
        imageView.contentDescription = pic.title
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("goneIfNotNull")
fun goneIfNotNull(view: View, it: Any?) {
    view.visibility = if (it != null) View.GONE else View.VISIBLE
}

@BindingAdapter("asteroidStatus")
fun bindStatus(statusImageView: ImageView, status: AsteroidStatus?) {
    when (status) {
        AsteroidStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        AsteroidStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        AsteroidStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
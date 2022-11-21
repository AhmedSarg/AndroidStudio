package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorRes
import androidx.core.graphics.toRect
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var widthSize = 0
    private var heightSize = 0
    private val animationDuration = 2000L
    private var text = context.getString(R.string.button_name)
    private var textWidth = 0.0f

    private val buttonRect = RectF()
    private val animationRect = RectF()
    private val textRect = Rect()

    private var startAnimationValue = 0f

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        .apply {
            duration = animationDuration
            interpolator = LinearInterpolator()
            addUpdateListener {
                startAnimationValue = it.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    buttonState = ButtonState.Loading
                    this@LoadingButton.isEnabled = false
                }

                override fun onAnimationEnd(animation: Animator?) {
                    buttonState = ButtonState.Completed
                    this@LoadingButton.isEnabled = true
                }
            })
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 50.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                text = context.getString(R.string.button_loading)
                invalidate()
            }
            ButtonState.Completed -> {
                valueAnimator.cancel()
                text = context.getString(R.string.button_name)
                invalidate()
            }
            ButtonState.Clicked -> {
                valueAnimator.start()
                invalidate()
            }
        }
    }

    init {
        isClickable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        buttonState = ButtonState.Clicked
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (buttonState == ButtonState.Loading) {
            progress(canvas = canvas)
        } else {
            default(canvas = canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun progress(canvas: Canvas) {
        buttonRect.set(0f, 0f, widthSize.toFloat(), heightSize.toFloat())
        paint.color = resources.getColor(R.color.colorPrimary)
        canvas.drawRect(buttonRect, paint)

        animationRect.set(0f, 0f, widthSize * startAnimationValue, heightSize.toFloat())
        paint.color = resources.getColor(R.color.colorPrimaryDark)
        canvas.drawRect(animationRect, paint)

        paint.color = resources.getColor(R.color.white)
        textWidth = paint.measureText(text)
        canvas.drawText(text, (widthSize.toFloat() / 2), (heightSize.toFloat() / 2) + 25.0f, paint)

        paint.getTextBounds(text, 0, text.length, textRect)
        val textRadius = textRect.height().toFloat()
        paint.color = resources.getColor(R.color.colorAccent)
        canvas.translate(widthSize.toFloat() / 2 + textWidth / 2 + 20 , heightSize.toFloat() / 2 - textRadius / 2 + 5)
        canvas.drawArc(0f, 0f, textRadius, textRadius, 0f, 360 * startAnimationValue, true, paint)
    }

    private fun default(canvas: Canvas) {
        buttonRect.set(0f, 0f, widthSize.toFloat(), heightSize.toFloat())
        paint.color = resources.getColor(R.color.colorPrimary)
        canvas.drawRect(buttonRect, paint)

        paint.color = resources.getColor(R.color.white)
        textWidth = paint.measureText(text)
        canvas.drawText(text, (widthSize.toFloat() / 2), (heightSize.toFloat() / 2) + 25.0f, paint)
    }

}
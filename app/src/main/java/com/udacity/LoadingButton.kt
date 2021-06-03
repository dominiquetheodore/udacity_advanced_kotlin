package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progress = 0
    private var currentSweepAngle = 0
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var btnColor1 = 0
    private var btnColor2 = 0
    private var btnColor3 = 0

    private var radius = 100f

    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                Log.i("btnState", "button loading")
                startLoadingAnimation()
            }
            ButtonState.Clicked -> {
                Log.i("btnState", "button clicked")
                startClickedAnimation()
            }
            ButtonState.Completed -> {
                Log.i("btnState", "download completed")
            }
        }
    }

    fun setDownloadState(state: ButtonState) {
        buttonState = state
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            btnColor1 = getColor(R.styleable.LoadingButton_btnColor1, 0)
            btnColor2 = getColor(R.styleable.LoadingButton_btnColor2, 0)
            btnColor3 = getColor(R.styleable.LoadingButton_btnColor3, 0)
        }

        startAnimation()
    }

    override fun performClick(): Boolean {
        // if (super.performClick()) return true
        setDownloadState(ButtonState.Clicked)
        Log.i("checkedRadio", "clicked the custom element")

        invalidate()
        return true
    }

    private fun startAnimation() {
        var anim = ValueAnimator.ofInt(0, 360).apply {
            duration = 6500
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                currentSweepAngle = valueAnimator.animatedValue as Int
                // Log.i("customViewLog", currentSweepAngle.toString())
                invalidate()
            }
        }

        anim.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            paint.setColor(Color.RED)
            canvas.drawRect(200f, 100f, 800f + progress, 600f, paint)
            canvas.drawText("Download", 450f, 100f, paint)
            paint.color = Color.YELLOW;
            var boundingRect = RectF((width / 4).toFloat(), (height / 2).toFloat() - 200f, (width / 4).toFloat() + radius, (height / 2).toFloat() + currentSweepAngle)
            canvas.drawArc(boundingRect, 0F, currentSweepAngle.toFloat(), true, paint)
        }
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

    private fun startLoadingAnimation(){
        Log.i("btnState", "loading")
        var anim = ValueAnimator.ofInt(0, 360).apply {
            duration = 6500
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Int
                invalidate()
            }
        }

        anim.start()
    }

    private fun startClickedAnimation(){
        Log.i("btnState", "loading")
        var anim = ValueAnimator.ofInt(0, 360).apply {
            duration = 6500
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Int
                invalidate()
            }
        }

        anim.start()
    }

}
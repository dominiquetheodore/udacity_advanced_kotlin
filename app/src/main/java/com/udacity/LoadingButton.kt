package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*
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

    private var radius = 40f

    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()
    private lateinit var btnTxt: String

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {
            ButtonState.Loading -> {
                startLoadingAnimation()
            }
            ButtonState.Clicked -> {
                btnTxt = "We are loading"
                startClickedAnimation()
            }
            ButtonState.Completed -> {
                btnTxt = "Download"
            }
        }
    }

    fun setDownloadState(state: ButtonState) {
        buttonState = state
    }

    init {
        isClickable = true
        btnTxt = "We are loading."
        startAnimation()
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        setDownloadState(ButtonState.Clicked)
        invalidate()
        return true
    }

    private fun startAnimation() {
        var anim = ValueAnimator.ofInt(0, 360).apply {
            duration = 6500
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                currentSweepAngle = valueAnimator.animatedValue as Int
                invalidate()
            }
        }

        anim.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null) {
            paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
            canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)
            paint.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            if (progress < 100){
                canvas.drawRect(0f, 0f, widthSize*progress/100f.toFloat(), width.toFloat(), paint)
                paint.color = ContextCompat.getColor(context, R.color.colorAccent);
                var boundingRect = RectF(widthSize*0.8f, heightSize/2.0f - radius,widthSize*0.8f + radius*2f,heightSize/2.0f + radius)
                canvas.drawArc(boundingRect, 0F, currentSweepAngle.toFloat(), true, paint)
            }
            else {
                btnTxt = "Download"
            }
            paint.color = Color.WHITE;
            // calculate text height to center button text
            val textHeight: Float = paint.descent() - paint.ascent()
            val textOffset: Float = textHeight / 2 - paint.descent()
            canvas.drawText(btnTxt, widthSize.toFloat()/2.0f, heightSize/2.0f + textOffset, paint)
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
        var anim = ValueAnimator.ofInt(0, 100).apply {
            duration = 5000
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                progress = valueAnimator.animatedValue as Int
                invalidate()
            }
        }

        anim.start()
    }

    private fun startClickedAnimation(){
        var anim = ValueAnimator.ofInt(0, 100).apply {
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
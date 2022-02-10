package com.udacity

import android.animation.*
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var horizontalProgressAnimator: ValueAnimator? = null
    private var circularAnimation: ValueAnimator? = null
    private var circularProgress = 0f
    private var canvas: Canvas? = null
    private var widthSize = 0
    private var horizontalProgressWidth = 0
    private var heightSize = 0
    private var newBackgroundColor = 0
    private var textColor = 0
    private var text: CharSequence = ""
    private val circularProgressPaint = Paint()
    private val topX = 0
    private var textSize = 0
    private val topY = 0f
    private var progressColor: Int = 0
    private val cornerRadius = 10
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val defaultProgressColor: Int = Color.parseColor("#004349")
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var animationValue = 0
    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, _ ->
    }

    init {
        buttonState == ButtonState.Completed
        val loading = getContext().theme
            .obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0)
        try {
            textSize = loading.getDimensionPixelSize(
                R.styleable.LoadingButton_textSize,
                15
            )
            progressColor = loading.getColor(
                R.styleable.LoadingButton_progColor,
                defaultProgressColor
            )
            newBackgroundColor = loading.getColor(R.styleable.LoadingButton_background_color, Color.BLACK)
            textColor = loading.getColor(R.styleable.LoadingButton_text_color, Color.WHITE)
            text = loading.getText(R.styleable.LoadingButton_text)
        } finally {
            loading.recycle()

        }
    }

    fun setValue(animatedValue: Int) {
        animationValue = animatedValue
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.canvas = canvas
        newBackgroundColor = resources.getColor(R.color.colorPrimary)
        textColor = resources.getColor(R.color.white)
        if (buttonState == ButtonState.Loading) {
            text = resources.getText(R.string.button_loading)
        } else if (buttonState == ButtonState.Completed) {
            text = resources.getText(R.string.button_click)
        } else {
            text = resources.getText(R.string.download_text)
        }
        setButton(canvas)
    }

    private fun setButton(canvas: Canvas?) {
        this.canvas = canvas
        val backGroundRectF = RectF(
            topX.toFloat(), topY,
            canvas!!.width.toFloat(),
            canvas.height.toFloat()
        )
        canvas.drawRoundRect(
            backGroundRectF,
            cornerRadius.toFloat(),
            cornerRadius.toFloat(),
            backgroundPaint
        )
        canvas.drawColor(newBackgroundColor)
        val bounds = Rect()
        textPaint.color = textColor
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = textSize.toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            textPaint.getTextBounds(text, topX, text.toString().length, bounds)
            textPaint.getTextBounds(
                text,
                topX,
                text.length,
                bounds
            )
        }

        val xPosition = (canvas.width - bounds.width()) / 2
        val adjustText = (textPaint.descent() + textPaint.ascent()).toInt() / 2
        val yPosition = canvas.height / 2 - adjustText
        canvas.drawText(text.toString(), xPosition.toFloat(), yPosition.toFloat(), textPaint)

        val secondRectF = RectF(
            topX.toFloat(), topY,
            horizontalProgressWidth.toFloat(),
            canvas.height.toFloat()
        )
        backgroundPaint.color = progressColor
        canvas.drawRoundRect(
            secondRectF,
            cornerRadius.toFloat(),
            cornerRadius.toFloat(),
            backgroundPaint
        )

        circularProgressPaint.color = resources.getColor(R.color.colorAccent)
        val thirdRectF = RectF(
            (widthSize / 2).toFloat() - 15f, 10f,
            (widthSize / 2).toFloat() + 35f,
            (heightSize / 2).toFloat()
        )
        canvas.drawArc(thirdRectF, 0f, circularProgress, true, circularProgressPaint)
        if (buttonState == ButtonState.Clicked) {
            setLoadingState(ButtonState.Loading)
            increaseWidth()
        }
    }

    private fun increaseWidth() {
        buttonState = ButtonState.Loading
        horizontalProgressAnimator = ValueAnimator.ofInt(widthSize - widthSize, widthSize)
        horizontalProgressAnimator!!.addUpdateListener { valueAnimator ->
            val animatorValue = valueAnimator.animatedValue as Int
            println("NewAnimation>>$animatorValue")
            if (animatorValue == widthSize || animatorValue == widthSize - 1) {
                if (buttonState == ButtonState.Loading) {
                    horizontalProgressAnimator!!.cancel()
                    circularAnimation!!.cancel()
                    horizontalProgressWidth = 0
                    circularProgress = 0f
                    setLoadingState(ButtonState.Completed)
                } else if (buttonState == ButtonState.Completed) {
                    horizontalProgressAnimator!!.cancel()
                    circularAnimation!!.cancel()
                    horizontalProgressWidth = 0
                    circularProgress = 0f
                    setLoadingState(ButtonState.Completed)
                } else {
                    this@LoadingButton.progressColor = resources.getColor(R.color.colorPrimaryDark)
                    this.horizontalProgressWidth = animatorValue
                    this@LoadingButton.invalidate()
                    this@LoadingButton.requestLayout()
                }
            } else {
                this@LoadingButton.progressColor = resources.getColor(R.color.colorPrimaryDark)
                this.horizontalProgressWidth = animatorValue
                this@LoadingButton.invalidate()
                this@LoadingButton.requestLayout()
            }

        }
        circularAnimation = ValueAnimator.ofFloat(0f, 360f)
        circularAnimation!!.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float

            this@LoadingButton.circularProgress = value
            this@LoadingButton.invalidate()
            this@LoadingButton.requestLayout()
        }

        horizontalProgressAnimator!!.duration = 1900
        circularAnimation!!.duration = 1500
        horizontalProgressAnimator!!.repeatCount = 1
        circularAnimation!!.repeatCount = 1
        horizontalProgressAnimator!!.start()
        circularAnimation!!.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minWidth: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val width: Int = resolveSizeAndState(minWidth, widthMeasureSpec, 1)
        val height: Int = resolveSizeAndState(
            MeasureSpec.getSize(width),
            heightMeasureSpec,
            0
        )
        widthSize = width
        heightSize = height
        setMeasuredDimension(width, height)
    }


    fun setLoadingState(state: ButtonState) {
        buttonState = state

        if (buttonState == ButtonState.Clicked) {
            if (circularAnimation != null) {
                circularAnimation!!.cancel()
                circularProgress = 0f
            }
            if (horizontalProgressAnimator != null) {
                horizontalProgressAnimator!!.cancel()
                horizontalProgressWidth = 0
            }
        }
        invalidate()
        requestLayout()
    }
}
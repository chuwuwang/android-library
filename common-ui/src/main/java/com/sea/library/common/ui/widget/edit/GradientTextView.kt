package com.sea.library.common.ui.widget.edit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.sea.library.common.ui.R

@Suppress("unused", "MemberVisibilityCanBePrivate")
class GradientTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet ? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {
        const val SLIDING_START = 0
        const val SLIDING_END = 1
    }

    private val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView, defStyleAttr, 0)

    private var text = ""
    private var textSize = 0
    private var upColor = Color.BLACK   // 上层颜色
    private var belowColor = Color.RED  // 底层颜色
    private val paint = Paint().apply { isAntiAlias = true }

    private var slidingMode = SLIDING_START

    init {
        try {
            text = typedArray.getString(R.styleable.GradientTextView_gradient_text) ?: ""
            textSize = typedArray.getDimension(R.styleable.GradientTextView_gradient_text_size, 28f).toInt()
            upColor = typedArray.getColor(R.styleable.GradientTextView_gradient_up_color, Color.BLACK)
            belowColor = typedArray.getColor(R.styleable.GradientTextView_gradient_below_color, Color.RED)
            paint.textSize = textSize.toFloat()
        } finally {
            typedArray.recycle()
        }
        isEnabled = false
        isClickable = true
        isFocusable = false
    }

    fun setSlidingMode(mode: Int) {
        slidingMode = mode
    }

    var slidingPercent: Float = 0.0f
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas ? ) {
        super.onDraw(canvas)
        if (canvas != null) {
            // 绘制垂直居中线
            // drawCenterLineLineX(canvas)
            // 绘制水平居中线
            // drawCenterLineLineY(canvas)

            val viewWidth = width / 2
            val textWidth = paint.measureText(text)
            val fontMetrics: Paint.FontMetrics = paint.fontMetrics
            val start = viewWidth - textWidth / 2
            var end: Float = (height / 2) - (fontMetrics.ascent + fontMetrics.descent) / 2

            // 绘制中心文字 [底层]
            drawBelowCenterText(canvas, start, end)

            when (slidingMode) {
                SLIDING_START -> {
                    end = start + textWidth * slidingPercent
                    drawUpCenterText(
                        canvas, upColor, start.toInt(), end.toInt()
                    )
                }
                SLIDING_END -> {
                    // 结束位置 (1-percent) 表示从右往左滑动
                    end = start + textWidth * (1 - slidingPercent)
                    drawUpCenterText(
                        canvas, upColor, end.toInt(), (start + textWidth).toInt()
                    )
                }
            }
        }
    }

    private fun drawUpCenterText(canvas: Canvas, color: Int, start: Int, end: Int) {
        paint.color = color
        canvas.save()
        canvas.clipRect(start, 0, end, height)

        val descent = paint.descent()
        val ascent = paint.ascent()
        val textY = (height / 2) - (descent + ascent) / 2
        val start2 = width / 2 - paint.measureText(text) / 2

        canvas.drawText(text, start2, textY, paint)
        canvas.restore()
    }

    private fun drawBelowCenterText(canvas: Canvas, start: Float, end: Float) {
        paint.color = belowColor
        canvas.save()
        canvas.drawText(text, start, end, paint)
        canvas.restore()
    }

    private fun drawCenterLineLineX(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
        paint.strokeWidth = 3f
        canvas.drawLine((width / 2).toFloat(), 0f, (width / 2).toFloat(), height.toFloat(), paint)
    }

    private fun drawCenterLineLineY(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
        paint.strokeWidth = 3f
        canvas.drawLine(0f, height / 2.toFloat(), width.toFloat(), height / 2.toFloat(), paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent ? ): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    when (slidingMode) {
                        SLIDING_START -> {
                            slidingPercent = event.x / width
                        }
                        SLIDING_END -> {
                            slidingPercent = 1 - event.x / width
                        }
                    }
                    invalidate()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

}
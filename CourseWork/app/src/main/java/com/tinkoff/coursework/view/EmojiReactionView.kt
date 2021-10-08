package com.tinkoff.coursework.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tinkoff.coursework.R
import com.tinkoff.coursework.util.dpToPx

class EmojiReactionView constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : View(context, attributeSet) {

    companion object {
        private val SUPPORTED_DRAWABLE_STATE = intArrayOf(android.R.attr.state_selected)
        private const val DEFAULT_COUNT_OF_VOTES = 1
        private const val DEFAULT_EMOJI: Int = 0x1F441
        private const val DEFAULT_TEXT_COLOR_ID = R.color.emoji_view_default_text_color
        private const val DEFAULT_TEXTSIZE = 14f
    }

    init {
        context.obtainStyledAttributes(
            attributeSet,
            R.styleable.EmojiReactionView,
            0,
            0).apply {
            emojiCode = getInt(
                R.styleable.EmojiReactionView_emojiUnicode,
                DEFAULT_EMOJI
            )
            countOfVotes = getInt(
                R.styleable.EmojiReactionView_counterOfVotes,
                DEFAULT_COUNT_OF_VOTES
            )
            textColor = getColor(
                R.styleable.EmojiReactionView_textColor,
                ContextCompat.getColor(context, DEFAULT_TEXT_COLOR_ID)
            )
            recycle()
        }
    }

    var countOfVotes: Int
        set(value) {
            if (field != value) {
                field = value
                content = getFormattedContent()
            }
        }
    var emojiCode: Int
        set(value) {
            if (field != value) {
                field = value
                content = getFormattedContent()
            }
        }
    var textColor: Int
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }
    var contentTextSize: Float = DEFAULT_TEXTSIZE.dpToPx(context)
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private var textBounds = Rect()
    private var content: String = getFormattedContent()
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val contentPaint = Paint().apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = contentTextSize
        color = textColor
    }

    private val backgroundRect = RectF()
    private val coordinatePointOfContent = PointF()
    private val fontMetrics = Paint.FontMetrics()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        contentPaint.getTextBounds(content, 0, content.length, textBounds)

        val measuredWidthBounds = textBounds.width()
        val measuredHeightBounds = textBounds.height()

        val fullWidthContent = measuredWidthBounds + paddingStart + paddingEnd
        val fullHeightContent = measuredHeightBounds + paddingTop + paddingBottom

        val width = resolveSize(fullWidthContent, widthMeasureSpec)
        val height = resolveSize(fullHeightContent, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(content, coordinatePointOfContent.x, coordinatePointOfContent.y, contentPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        backgroundRect.set(w.toFloat(), h.toFloat(), 0f, 0f)
        contentPaint.getFontMetrics(fontMetrics)
        coordinatePointOfContent.x = width / 2f
        coordinatePointOfContent.y = h / 2f + textBounds.height() / 2 - fontMetrics.descent
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + SUPPORTED_DRAWABLE_STATE.size)
        if (isSelected) {
            mergeDrawableStates(drawableState, SUPPORTED_DRAWABLE_STATE)
        }
        return super.onCreateDrawableState(extraSpace)
    }

    private fun getFormattedContent(): String {
        return "${String(Character.toChars(emojiCode))} $countOfVotes"
    }
}
package com.tinkoff.coursework.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.children
import com.tinkoff.coursework.R

/**
 * Флекс-бокс лейаут
 * Отображает элементы по
 * @property maxQuantityChildPerRow в ряд
 */
class FlexBoxLayout constructor(
    context: Context,
    attributeSet: AttributeSet
) : ViewGroup(context, attributeSet) {

    companion object {
        private const val DEFAULT_MAX_QUANTITY_OF_ROW = 5
    }

    init {
        context.obtainStyledAttributes(attributeSet, R.styleable.FlexBoxLayout, 0, 0).apply {
            maxQuantityChildPerRow = getInt(
                R.styleable.FlexBoxLayout_maxQuantityOfChildPerRow,
                DEFAULT_MAX_QUANTITY_OF_ROW
            )
            offsetBetweenElenements = getDimensionPixelOffset(
                R.styleable.FlexBoxLayout_offsetBetweenElements,
                resources.getDimensionPixelSize(
                    R.dimen.flex_box_layout_offset_between_elements
                )
            )
            recycle()
        }
    }

    var maxQuantityChildPerRow: Int
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var offsetBetweenElenements: Int

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        (children).forEach { child ->
            child.layout(child.left, child.top, child.right, child.bottom)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        var currentWidth = 0
        var topOfLatestRow = 0
        var widthOfLayout = 0
        var heightOfLayout = 0
        var childCounterPerRow = 0
        children.forEach {
            measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0)
        }
        val maxChildHeight = if (children.iterator().hasNext()) {
            children.maxOf { children ->
                children.measuredHeight
            }
        } else 0
        children.forEach { child ->
            if (currentWidth + child.measuredWidth >= widthSpecSize || childCounterPerRow >= maxQuantityChildPerRow) {
                currentWidth = 0
                childCounterPerRow = 0
                topOfLatestRow += maxChildHeight + offsetBetweenElenements
                heightOfLayout += topOfLatestRow + maxChildHeight
            }
            putView(child, currentWidth, topOfLatestRow)
            childCounterPerRow++
            currentWidth += child.measuredWidth + offsetBetweenElenements
            widthOfLayout = maxOf(widthOfLayout, currentWidth)
        }
        setMeasuredDimension(
            resolveSize(widthOfLayout, widthMeasureSpec),
            resolveSize(
                maxOf(maxChildHeight, heightOfLayout),
                heightMeasureSpec
            )
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)

    private fun putView(child: View, currentWidth: Int, topOfChildren: Int) {
        child.left = currentWidth
        child.top = topOfChildren
        child.right = currentWidth + child.measuredWidth
        child.bottom = child.top + child.measuredHeight
    }
}
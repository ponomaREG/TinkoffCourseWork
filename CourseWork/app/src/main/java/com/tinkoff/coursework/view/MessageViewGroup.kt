package com.tinkoff.coursework.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.*
import com.tinkoff.coursework.R
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import com.tinkoff.coursework.util.sumHorizontalMargins
import com.tinkoff.coursework.util.sumVerticalMargins

class MessageViewGroup constructor(
    context: Context,
    attributeSet: AttributeSet
) : ViewGroup(context, attributeSet) {

    private val avatarImageView: ImageView
    private val usernameTextView: TextView
    private val messageTextView: TextView
    private val reactionsFlexBoxLayout: FlexBoxLayout

    private val avatarRect = Rect()
    private val usernameRect = Rect()
    private val messageRect = Rect()
    private val reactionsRect = Rect()
    private val backgroundRect = RectF()
    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context, R.color.message_view_group_background_color)
    }
    private val radiusMessageBackground =
        resources.getDimensionPixelSize(R.dimen.message_viewgroup_radius).toFloat()
    private val backgroundMargin =
        resources.getDimensionPixelSize(R.dimen.message_viewgroup_background_margin)

    init {
        LayoutInflater.from(context).inflate(R.layout.message_viewgroup, this, true)
        avatarImageView = findViewById(R.id.avatar)
        usernameTextView = findViewById(R.id.username)
        messageTextView = findViewById(R.id.message)
        reactionsFlexBoxLayout = findViewById(R.id.reactions)
        avatarImageView.clipToOutline = true
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val avatarLayoutParams = avatarImageView.layoutParams as MarginLayoutParams
        val userNameLayoutParams = usernameTextView.layoutParams as MarginLayoutParams
        val messageLayoutParams = messageTextView.layoutParams as MarginLayoutParams
        val reactionsFlexBoxLayoutParams = reactionsFlexBoxLayout.layoutParams as MarginLayoutParams

        val (avatarHeight, avatarWidth) = measureChildWithMarginsAndGetSize(
            avatarImageView,
            widthMeasureSpec,
            0,
            heightMeasureSpec,
            0,
            avatarLayoutParams
        )
        val (usernameHeight, usernameWidth) = measureChildWithMarginsAndGetSize(
            usernameTextView,
            widthMeasureSpec,
            avatarWidth,
            heightMeasureSpec,
            0,
            userNameLayoutParams
        )
        val (messageHeight, messageWidth) = measureChildWithMarginsAndGetSize(
            messageTextView,
            widthMeasureSpec,
            avatarWidth,
            heightMeasureSpec,
            usernameHeight,
            messageLayoutParams
        )
        val (reactionsFlexBoxHeight, reactionsFlexBoxWidth) = measureChildWithMarginsAndGetSize(
            reactionsFlexBoxLayout,
            widthMeasureSpec,
            avatarWidth,
            heightMeasureSpec,
            usernameHeight + messageHeight,
            reactionsFlexBoxLayoutParams
        )
        val backgroundRectLeft =
            avatarImageView.measuredHeight +
                avatarImageView.marginEnd + avatarImageView.marginStart - backgroundMargin
        val backgroundRectRight =
            backgroundRectLeft +
                maxOf(usernameWidth, messageWidth).toFloat() + 2 * backgroundMargin
        backgroundRect.set(
            backgroundRectLeft.toFloat(),
            0f,
            backgroundRectRight,
            (messageHeight + usernameHeight).toFloat()
        )
        setMeasuredDimension(
            resolveSize(
                avatarWidth + maxOf(
                    usernameWidth,
                    messageWidth,
                    reactionsFlexBoxWidth
                ),
                widthMeasureSpec
            ),
            resolveSize(
                maxOf(
                    avatarHeight,
                    usernameHeight + messageHeight + reactionsFlexBoxHeight
                ),
                heightMeasureSpec
            )
        )
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        avatarImageView.layout(
            avatarRect,
            null,
            null,
            null
        )
        usernameTextView.layout(
            usernameRect,
            avatarImageView,
            avatarRect,
            null
        )
        messageTextView.layout(
            messageRect,
            avatarImageView,
            avatarRect,
            usernameRect
        )
        reactionsFlexBoxLayout.layout(
            reactionsRect,
            avatarImageView,
            avatarRect,
            messageRect
        )
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(
            backgroundRect,
            radiusMessageBackground,
            radiusMessageBackground,
            backgroundPaint
        )
    }

    fun setMessage(message: Message) {
        avatarImageView.setImageResource(message.avatarRes)
        usernameTextView.text = message.username
        messageTextView.text = message.message
        addReactions(message.reactions)
    }

    fun addReactions(reactions: List<Reaction>) {
        reactions.forEach { reaction ->
            val emojiView = EmojiReactionView(context)
            emojiView.setBackgroundResource(R.drawable.bg_emoji_view)
            emojiView.setPadding(
                resources.getDimensionPixelSize(R.dimen.emoji_view_padding)
            )
            emojiView.contentTextSize =
                resources.getDimensionPixelSize(R.dimen.emoji_view_text_size).toFloat()
            emojiView.countOfVotes = reaction.countOfVotes
            emojiView.emojiCode = reaction.emojiCode
            emojiView.setOnClickListener { emoji ->
                emoji.isSelected = emoji.isSelected.not()
                if (emoji.isSelected) emojiView.countOfVotes += 1
                else emojiView.countOfVotes -= 1
            }
            reactionsFlexBoxLayout.addView(emojiView)
        }
        requestLayout()
    }

    private fun View.layout(
        currentChildRect: Rect,
        childOnLeft: View?,
        childOnLeftRect: Rect?,
        childOnTopRect: Rect?) {
        currentChildRect.left = (childOnLeftRect?.right ?: 0) +
            (childOnLeft?.marginRight ?: 0) + marginLeft
        currentChildRect.top = (childOnTopRect?.bottom ?: 0) + marginTop
        currentChildRect.right = currentChildRect.left + measuredWidth
        currentChildRect.bottom = currentChildRect.top + measuredHeight + marginBottom
        this.layout(
            currentChildRect.left,
            currentChildRect.top,
            currentChildRect.right,
            currentChildRect.bottom
        )
    }

    private fun measureChildWithMarginsAndGetSize(
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int,
        params: MarginLayoutParams,
    ): Pair<Int, Int> {
        measureChildWithMargins(
            child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed
        )
        val height = child.measuredHeight + params.sumVerticalMargins()
        val width = child.measuredWidth + params.sumHorizontalMargins()
        return height to width
    }
}
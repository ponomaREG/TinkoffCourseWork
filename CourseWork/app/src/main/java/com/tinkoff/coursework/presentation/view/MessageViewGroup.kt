package com.tinkoff.coursework.presentation.view

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
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.model.ReactionUI
import com.tinkoff.coursework.presentation.util.sumHorizontalMargins
import com.tinkoff.coursework.presentation.util.sumVerticalMargins

class MessageViewGroup constructor(
    context: Context,
    attributeSet: AttributeSet?
) : ViewGroup(context, attributeSet) {

    private val avatarImageView: ImageView
    private val imageViewIconAdd: ImageView
    private val usernameTextView: TextView
    private val messageTextView: TextView
    private val reactionsFlexBoxLayout: FlexBoxLayout

    private val emojiContentTextSize: Float
    private val emojiBackgroundId: Int
    private val emojiPadding: Float

    private var emojiReactionViewListener: OnEmojiClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.message_viewgroup, this, true)
        avatarImageView = findViewById(R.id.avatar)
        usernameTextView = findViewById(R.id.username)
        messageTextView = findViewById(R.id.message)
        reactionsFlexBoxLayout = findViewById(R.id.reactions)
        avatarImageView.clipToOutline = true
        imageViewIconAdd = ImageView(context).apply {
            setImageResource(R.drawable.ic_plus_emoji)
        }
        context.obtainStyledAttributes(attributeSet, R.styleable.MessageViewGroup).apply {
            emojiContentTextSize = getDimensionPixelSize(
                R.styleable.MessageViewGroup_emojiTextSize,
                resources.getDimensionPixelSize(R.dimen.emoji_view_text_size)
            ).toFloat()
            emojiBackgroundId = getResourceId(
                R.styleable.MessageViewGroup_emojiBackground,
                R.drawable.bg_emoji_view
            )
            emojiPadding = getDimensionPixelSize(
                R.styleable.MessageViewGroup_emojiPadding,
                resources.getDimensionPixelSize(R.dimen.emoji_view_padding)
            ).toFloat()
            recycle()
        }
        setWillNotDraw(false)
    }

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
                    usernameWidth + backgroundMargin,
                    messageWidth + backgroundMargin,
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
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

    fun setMessage(
        messageUI: MessageUI,
        avatarSetter: (ImageView) -> Unit = { avatar ->
            avatar.setImageResource(R.mipmap.ic_launcher)
        }
    ) {
        avatarSetter(avatarImageView)
        usernameTextView.text =
            if (messageUI.isMyMessage) {
                context.getString(R.string.message_viewgroup_my_message_indicator)
            } else messageUI.username
        avatarImageView.isInvisible = messageUI.isMyMessage
        messageTextView.text = messageUI.message
        setReactions(messageUI.reactions)
    }

    fun setReactions(
        reactions: List<ReactionUI>
    ) {
        reactionsFlexBoxLayout.removeAllViews()
        if (reactions.isNotEmpty()) {
            reactions.forEachIndexed { index, reaction ->
                val emojiView = EmojiReactionView(context)
                emojiView.setBackgroundResource(emojiBackgroundId)
                emojiView.setPadding(emojiPadding.toInt())
                emojiView.contentTextSize = emojiContentTextSize
                emojiView.countOfVotes = reaction.countOfVotes
                emojiView.emojiCode = reaction.emojiUI.emojiCode
                emojiView.isSelected = reaction.isSelected
                emojiReactionViewListener?.let {
                    emojiView.setOnClickListener(emojiReactionViewListener!!, index)
                }
                reactionsFlexBoxLayout.addView(emojiView)
            }
            reactionsFlexBoxLayout.addView(imageViewIconAdd)
            requestLayout()
        }
    }

    fun setOnEmojiViewClickListener(listener: OnEmojiClickListener) {
        emojiReactionViewListener = listener
        for (i in 0 until reactionsFlexBoxLayout.childCount - 1) {
            (reactionsFlexBoxLayout.getChildAt(i) as EmojiReactionView)
                .setOnClickListener(listener, i)
        }
    }

    fun setOnAddClickListenerClick(listener: OnClickListener) {
        imageViewIconAdd.setOnClickListener(listener)
    }

    private fun View.layout(
        currentChildRect: Rect,
        childOnLeft: View?,
        childOnLeftRect: Rect?,
        childOnTopRect: Rect?
    ) {
        currentChildRect.left = (childOnLeftRect?.right ?: 0) + (childOnLeft?.marginRight
            ?: 0) + this.marginLeft
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

    private fun EmojiReactionView.setOnClickListener(
        emojiClickListener: OnEmojiClickListener,
        position: Int
    ) {
        setOnClickListener {
            emojiClickListener.click(position)
        }
    }

    interface OnEmojiClickListener {
        fun click(reactionInContainerPosition: Int)
    }
}
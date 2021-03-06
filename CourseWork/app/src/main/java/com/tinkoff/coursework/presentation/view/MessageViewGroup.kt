package com.tinkoff.coursework.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.*
import com.tinkoff.coursework.R
import com.tinkoff.coursework.presentation.model.MessageHyperlinkUI
import com.tinkoff.coursework.presentation.model.MessageUI
import com.tinkoff.coursework.presentation.model.ReactionUI
import com.tinkoff.coursework.presentation.util.sumHorizontalMargins
import com.tinkoff.coursework.presentation.util.sumVerticalMargins

/**
 * Вьюшка сообщения
 * Отображает сообщение, его реакции
 */
class MessageViewGroup constructor(
    context: Context,
    attributeSet: AttributeSet?
) : ViewGroup(context, attributeSet) {

    private val avatarImageView: ImageView
    private val imageViewIconAdd: ImageView
    private val usernameTextView: TextView
    private val messageTextView: TextView
    private val reactionsFlexBoxLayout: FlexBoxLayout
    private val topicNameTextView: TextView

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
        topicNameTextView = findViewById(R.id.topicName)
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
    private val topicNameRect = Rect()
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
        val topicNameLayoutParams = topicNameTextView.layoutParams as MarginLayoutParams
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
        val (topicNameHeight, topicNameWidth) = measureChildWithMarginsAndGetSize(
            topicNameTextView,
            widthMeasureSpec,
            usernameWidth,
            heightMeasureSpec,
            0,
            topicNameLayoutParams
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
                maxOf(
                    usernameWidth + topicNameWidth,
                    messageWidth
                ).toFloat() + 2 * backgroundMargin
        backgroundRect.set(
            backgroundRectLeft.toFloat(),
            0f,
            backgroundRectRight,
            (messageHeight + usernameHeight).toFloat()
        )
        setMeasuredDimension(
            resolveSize(
                avatarWidth + maxOf(
                    usernameWidth + topicNameWidth + backgroundMargin,
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
        topicNameTextView.layout(
            topicNameRect,
            usernameTextView,
            usernameRect,
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
        },
        onLinkInMessageClick: (MessageHyperlinkUI) -> Unit = {},
        onTopicClick: (MessageUI) -> Unit = {}
    ) {
        avatarSetter(avatarImageView)
        usernameTextView.text =
            if (messageUI.isMyMessage) {
                context.getString(R.string.message_viewgroup_my_message_indicator)
            } else messageUI.username
        avatarImageView.isInvisible = messageUI.isMyMessage
        if (messageUI.isUniqueTopicInAllChat) topicNameTextView.visibility = View.GONE
        else {
            topicNameTextView.text = messageUI.topicName
            topicNameTextView.setOnClickListener {
                onTopicClick(messageUI)
            }
        }
        if (messageUI.hyperlinks.isNotEmpty()) setMessageTextWithClickableSpan(
            messageUI,
            onLinkInMessageClick
        )
        else messageTextView.text = messageUI.message
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

    private fun setMessageTextWithClickableSpan(
        messageUI: MessageUI,
        click: (MessageHyperlinkUI) -> Unit
    ) {
        val spannableText = SpannableString(messageUI.message)
        messageUI.hyperlinks.forEach { hyperlink ->
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    click(hyperlink)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = true
                    ds.color = ds.linkColor
                }
            }
            spannableText.setSpan(
                clickableSpan,
                hyperlink.from,
                hyperlink.to - 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        messageTextView.movementMethod = LinkMovementMethod.getInstance()
        messageTextView.text = spannableText
    }

    interface OnEmojiClickListener {
        fun click(reactionInContainerPosition: Int)
    }
}
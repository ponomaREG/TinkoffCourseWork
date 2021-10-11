package com.tinkoff.coursework.adapter

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.view.EmojiReactionView
import com.tinkoff.coursework.view.MessageViewGroup

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.Holder>() {

    private val items: MutableList<Message> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(MessageViewGroup(parent.context, null))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addMessages(messages: List<Message>) {
        val beginPosition = itemCount
        items.addAll(messages)
        notifyItemRangeInserted(beginPosition, messages.size)
    }

    class Holder(private val view: MessageViewGroup) : RecyclerView.ViewHolder(view) {
        fun bind(message: Message) {
            view.setMessage(message)
            view.setOnAddClickListenerClick { icAdd ->

            }
            view.setOnEmojiViewClickListener { view ->
                if (view is EmojiReactionView) {
                    view.isSelected = view.isSelected.not()
                    if (view.isSelected) view.countOfVotes += 1
                    else view.countOfVotes -= 1
                }
            }
        }
    }
}
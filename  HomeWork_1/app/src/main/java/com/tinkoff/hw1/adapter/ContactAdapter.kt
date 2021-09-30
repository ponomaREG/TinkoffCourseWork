package com.tinkoff.hw1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.hw1.R
import com.tinkoff.hw1.model.Contact

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.Holder>() {

    private val _items: MutableList<Contact> = mutableListOf()


    fun setItems(items: List<Contact>) {
        _items.clear()
        _items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItems(): List<Contact> = _items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_contact,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(_items[position])
    }

    override fun getItemCount(): Int = _items.size

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val displayName: TextView = view.findViewById(R.id.item_contact_name)

        fun bind(contact: Contact) {
            displayName.text = contact.contactName
        }
    }
}
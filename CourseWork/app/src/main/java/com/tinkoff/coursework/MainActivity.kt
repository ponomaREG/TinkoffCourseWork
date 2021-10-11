package com.tinkoff.coursework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.coursework.adapter.MessageAdapter
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val adapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rvMessage = findViewById<RecyclerView>(R.id.rv_messages)
        rvMessage.adapter = adapter
        adapter.addMessages(
            listOf(
                mockMessage()
            )
        )
    }

    private fun mockReactions() = listOf(
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), false),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), false),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), true),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), false),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), true),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), false),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), false),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200), false),
    )

    private fun mockMessage() = Message(
        "Студент",
        "Добавил скролл",
        R.mipmap.ic_launcher,
        mockReactions()
    )
}
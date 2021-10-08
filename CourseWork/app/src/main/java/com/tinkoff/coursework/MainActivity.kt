package com.tinkoff.coursework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import com.tinkoff.coursework.view.MessageViewGroup
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<MessageViewGroup>(R.id.ex_message)
            .setMessage(mockMessage())
    }

    private fun mockReactions() = listOf(
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
        Reaction(Random.nextInt(0x1F300, 0x1f310), Random.nextInt(1, 200)),
    )

    private fun mockMessage() = Message(
        "Иванов Иван",
        "Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/естовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/естовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/естовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/естовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/Тестовое сообщение/ \n" +
            " Конец ",
        R.mipmap.ic_launcher,
        mockReactions()
    )
}
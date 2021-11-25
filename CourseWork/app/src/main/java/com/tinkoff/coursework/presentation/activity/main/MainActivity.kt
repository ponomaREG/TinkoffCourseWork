package com.tinkoff.coursework.presentation.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.work.*
import com.tinkoff.coursework.CourseWorkApp
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ActivityMainBinding
import com.tinkoff.coursework.presentation.fragment.channels.ChannelsFragment
import com.tinkoff.coursework.presentation.fragment.people.PeopleFragment
import com.tinkoff.coursework.presentation.fragment.profile.ProfileFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private var currentFragment: Fragment = ChannelsFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(binding.mainContainer.id, currentFragment)
                .commit()
        }
        initNavigation()
    }

    private fun initNavigation() {
        val app = application as? CourseWorkApp ?: return
        app.activateStreamComponent()
        binding.mainBnv.setOnItemSelectedListener { item ->
            currentFragment = when (item.itemId) {
                R.id.main_bnv_channels -> {
                    app.deactivateAllComponents()
                    app.activateStreamComponent()
                    ChannelsFragment.newInstance()
                }
                R.id.main_bnv_people -> {
                    app.deactivateAllComponents()
                    app.activatePeopleComponent()
                    PeopleFragment.newInstance()
                }
                R.id.main_bnv_profile -> {
                    app.deactivateAllComponents()
                    app.activateProfileComponent()
                    ProfileFragment.newInstance()
                }
                else -> return@setOnItemSelectedListener false
            }
            supportFragmentManager.beginTransaction()
                .replace(binding.mainContainer.id, currentFragment)
                .commit()
            true
        }
    }
}
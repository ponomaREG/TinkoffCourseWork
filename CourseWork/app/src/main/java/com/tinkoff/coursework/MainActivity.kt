package com.tinkoff.coursework

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tinkoff.coursework.databinding.ActivityMainBinding
import com.tinkoff.coursework.fragment.FragmentNavigationViaStreams
import com.tinkoff.coursework.fragment.FragmentPeople
import com.tinkoff.coursework.fragment.FragmentProfile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var currentFragment: Fragment = FragmentNavigationViaStreams.newInstance()

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
        binding.mainBnv.setOnItemSelectedListener { item ->
            currentFragment = when (item.itemId) {
                R.id.main_bnv_channels -> {
                    FragmentNavigationViaStreams.newInstance()
                }
                R.id.main_bnv_people -> {
                    FragmentPeople.newInstance()
                }
                R.id.main_bnv_profile -> {
                    FragmentProfile.newInstance()
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
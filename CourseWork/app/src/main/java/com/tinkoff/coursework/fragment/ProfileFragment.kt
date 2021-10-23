package com.tinkoff.coursework.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentProfileBinding
import com.tinkoff.coursework.util.MockUtil

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ownProfile = MockUtil.mockOwnProfile()
        binding.apply {
            fragmentProfileName.text = ownProfile.name
            fragmentProfileAvatar.setImageResource(ownProfile.avatar)
            fragmentProfileAvatar.clipToOutline = true
            fragmentProfileOnlineStatus.text =
                if (ownProfile.isOnline) getString(R.string.fragment_profile_onlineStatus)
                else {
                    fragmentProfileStatus.setTextColor(Color.RED)
                    getString(R.string.fragment_profile_offlineStatus)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
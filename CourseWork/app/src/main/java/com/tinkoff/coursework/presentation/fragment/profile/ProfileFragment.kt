package com.tinkoff.coursework.presentation.fragment.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentProfileBinding
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.User
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeState()
        observeActions()
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun renderProfile(profile: User) {
        binding.apply {
            fragmentProfileName.text = profile.name
            fragmentProfileAvatar.setImageResource(profile.avatar)
            fragmentProfileAvatar.clipToOutline = true
            fragmentProfileOnlineStatus.text =
                if (profile.isOnline) getString(R.string.fragment_profile_onlineStatus)
                else {
                    fragmentProfileStatus.setTextColor(Color.RED)
                    getString(R.string.fragment_profile_offlineStatus)
                }
        }
    }

    private fun observeState() {
        viewModel.stateObservable
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                state.apply {
                    binding.fragmentProfileStatus.isInvisible = loadingState == LoadingState.LOADING
                    binding.profileShimmer.apply {
                        isVisible = loadingState == LoadingState.LOADING
                        if (isVisible) startShimmer() else stopShimmer()
                    }
                    data?.let {
                        renderProfile(it)
                    }
                }
            }.addTo(compositeDisposable)
    }

    private fun observeActions() {
        viewModel.actionObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action ->
                when (action) {
                    is ProfileAction.ShowToastMessage ->
                        requireContext().showToast(action.message)
                }
            }.addTo(compositeDisposable)
    }

    private fun setListeners() {
        binding.fragmentProfileLogOut.setOnClickListener {
            viewModel.onLogoutClick()
        }
    }
}
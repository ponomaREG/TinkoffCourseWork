package com.tinkoff.coursework.presentation.fragment.profile

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.tinkoff.coursework.databinding.FragmentProfileBinding
import com.tinkoff.coursework.getAppComponent
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.di.profile.DaggerProfileComponent
import com.tinkoff.coursework.presentation.model.UserUI
import com.tinkoff.coursework.presentation.util.detectStatusColor
import com.tinkoff.coursework.presentation.util.loadImageByUrl
import com.tinkoff.coursework.presentation.util.showToast
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class ProfileFragment : ElmFragment<ProfileEvent, ProfileAction, ProfileUIState>() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }

    @Inject
    lateinit var actor: ProfileActor

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerProfileComponent.factory().create(getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override val initEvent: ProfileEvent
        get() = ProfileEvent.Ui.LoadOwnUserInfo

    override fun createStore(): Store<ProfileEvent, ProfileAction, ProfileUIState> {
        return ElmStoreCompat(
            initialState = ProfileUIState(),
            reducer = ProfileReducer(),
            actor = actor
        )
    }

    override fun render(state: ProfileUIState) {
        state.apply {
            binding.fragmentProfileOnlineStatus.isInvisible =
                state.loadingState == LoadingState.LOADING
            binding.profileShimmer.apply {
                isVisible = loadingState == LoadingState.LOADING
                if (isVisible) startShimmer() else stopShimmer()
            }
            data?.let {
                renderProfile(it)
            }
        }
    }

    override fun handleEffect(effect: ProfileAction): Unit = when (effect) {
        is ProfileAction.ShowToastMessage ->
            requireContext().showToast(requireContext().getString(effect.messageId))
    }

    private fun renderProfile(profile: UserUI) {
        binding.apply {
            fragmentProfileName.text = profile.fullName
            fragmentProfileAvatar.loadImageByUrl(profile.avatarUrl)
            fragmentProfileAvatar.clipToOutline = true
            fragmentProfileOnlineStatus.imageTintList = ColorStateList.valueOf(
                profile.status.detectStatusColor(requireContext())
            )
        }
    }
}
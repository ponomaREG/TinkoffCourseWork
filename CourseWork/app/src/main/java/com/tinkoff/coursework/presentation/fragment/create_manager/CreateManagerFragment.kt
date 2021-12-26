package com.tinkoff.coursework.presentation.fragment.create_manager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import com.tinkoff.coursework.databinding.FragmentCreateManagerBinding
import com.tinkoff.coursework.getAppComponent
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.di.create_manager.DaggerCreateManagerComponent
import com.tinkoff.coursework.presentation.util.showToast
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class CreateManagerFragment :
    ElmFragment<CreateManagerEvent, CreateManagerAction, CreateManagerState>() {

    companion object {
        private const val ARGS_CREATE_TYPE = "ARGS_CREATE_TYPE"
        fun newInstance(createType: CreateType): CreateManagerFragment {
            val fragment = CreateManagerFragment()
            val args = Bundle()
            args.putParcelable(ARGS_CREATE_TYPE, createType)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    internal lateinit var actor: CreateManagerActor

    private var _binding: FragmentCreateManagerBinding? = null
    private val binding: FragmentCreateManagerBinding
        get() = _binding!!

    private val createType: CreateType
        get() = requireArguments().getParcelable(ARGS_CREATE_TYPE) ?: throw IllegalStateException()


    override val initEvent: CreateManagerEvent
        get() = CreateManagerEvent.Ui.InitialEvent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerCreateManagerComponent.factory().create(getAppComponent()).inject(this)
        createType
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateManagerBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        attachTextWatcher()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun createStore(): Store<CreateManagerEvent, CreateManagerAction, CreateManagerState> =
        ElmStoreCompat(
            initialState = CreateManagerState(
                loading = LoadingState.SUCCESS
            ),
            reducer = CreateManagerReducer(),
            actor = actor
        )


    override fun render(state: CreateManagerState) {
        binding.apply {
            val isLoading = state.loading == LoadingState.LOADING
            progressIndicator.isGone = isLoading.not()
            inputTitle.isEnabled = isLoading.not()
            commit.isEnabled = isLoading.not()
        }
    }

    override fun handleEffect(effect: CreateManagerAction) = when (effect) {
        CreateManagerAction.CloseWithResult -> {
            requireActivity().supportFragmentManager.popBackStack()
        }
        is CreateManagerAction.ShowToastMessage -> {
            requireContext().showToast(requireContext().getString(effect.error))
        }
    }

    private fun initListeners() {
        binding.commit.setOnClickListener {
            store.accept(
                CreateManagerEvent.Ui.Create(
                    binding.inputTitle.text.toString(),
                    createType
                )
            )
        }
    }

    private fun attachTextWatcher() {
        binding.inputTitle.doOnTextChanged { text, _, _, _ ->
            binding.commit.isEnabled = text?.isNotEmpty() ?: false
        }
    }
}
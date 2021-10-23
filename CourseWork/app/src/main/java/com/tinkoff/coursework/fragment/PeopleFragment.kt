package com.tinkoff.coursework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tinkoff.coursework.R
import com.tinkoff.coursework.adapter.DelegateAdapter
import com.tinkoff.coursework.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.adapter.viewtype.UserViewType
import com.tinkoff.coursework.databinding.FragmentPeopleBinding
import com.tinkoff.coursework.model.User
import com.tinkoff.coursework.util.MockUtil
import com.tinkoff.coursework.util.doAfterTextChangedWithDelay
import java.util.*

class PeopleFragment : Fragment() {

    companion object {
        fun newInstance(): PeopleFragment {
            return PeopleFragment()
        }
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val peopleAdapter = DelegateAdapter(
        listOf(
            UserViewType(this::onUserClick)
        )
    )

    private val people: List<User> = MockUtil.mockUsers()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        peopleAdapter.setItems(people)
        attachTextWatcher()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = peopleAdapter
        binding.recyclerView.addItemDecoration(
            OffsetItemDecorator(
                top = resources.getDimensionPixelSize(R.dimen.item_user_margin_top),
                bottom = resources.getDimensionPixelSize(R.dimen.item_user_margin_bottom),
                left = resources.getDimensionPixelSize(R.dimen.item_user_margin_left),
                right = resources.getDimensionPixelSize(R.dimen.item_user_margin_right),
            )
        )
    }

    private fun attachTextWatcher() {
        binding.fragmentPeopleSearchInput.doAfterTextChangedWithDelay { input ->
            if (input.isNotEmpty()) {
                peopleAdapter.setItems(
                    people.filter { user ->
                        user.name.toLowerCase(Locale.ROOT).contains(input)
                    }
                )
            } else peopleAdapter.setItems(people)
        }
    }

    private fun onUserClick(user: User) {
    }
}
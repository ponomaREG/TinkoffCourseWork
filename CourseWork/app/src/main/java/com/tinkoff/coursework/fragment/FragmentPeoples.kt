package com.tinkoff.coursework.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.util.*

class FragmentPeople : Fragment() {

    companion object {
        fun newInstance(): FragmentPeople {
            return FragmentPeople()
        }
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val peopleAdapter = DelegateAdapter(
        listOf(
            UserViewType(this::onUserClick)
        )
    )

    private lateinit var people: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        people = MockUtil.mockUsers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        binding.fragmentPeopleRvPeople.adapter = peopleAdapter
        binding.fragmentPeopleRvPeople.addItemDecoration(
            OffsetItemDecorator(
                top = resources.getDimensionPixelSize(R.dimen.item_user_margin_top),
                bottom = resources.getDimensionPixelSize(R.dimen.item_user_margin_bottom),
                left = resources.getDimensionPixelSize(R.dimen.item_user_margin_left),
                right = resources.getDimensionPixelSize(R.dimen.item_user_margin_right),
            )
        )
    }

    private fun attachTextWatcher() {
        binding.fragmentPeopleSearchInput.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.isNullOrEmpty().not()) {
                        peopleAdapter.setItems(
                            people.filter {
                                it.name.toLowerCase(Locale.ROOT).contains(s.toString())
                            }
                        )
                    } else peopleAdapter.setItems(people)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
        )
    }

    private fun onUserClick(user: User) {
    }
}
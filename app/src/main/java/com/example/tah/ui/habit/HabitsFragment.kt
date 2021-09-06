package com.example.tah.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.R
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HabitsFragment
    : Fragment(R.layout.fragment_habits),
    ViewInitializable
{
    private val viewModel: HabitViewModel by viewModels()

    @Inject
    lateinit var adapter: HabitRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_habits, container, false)

        if (view is RecyclerView) {
            view.layoutManager = LinearLayoutManager(view.context)
            view.adapter = adapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelObservables()
    }

    override fun initViewModelObservables() {
        viewModel.itemsLD!!.observe(viewLifecycleOwner) { adapter.differ.submitList(it) }
    }

    override fun initOnClickListeners() {
        TODO("Not yet implemented")
    }

}
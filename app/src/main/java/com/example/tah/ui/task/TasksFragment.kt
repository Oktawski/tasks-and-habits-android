package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.R
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TasksFragment
    : Fragment(R.layout.fragment_tasks),
    ViewInitializable {

    private val viewModel: TaskViewModel by viewModels()

    @Inject
    lateinit var adapter: TaskRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        if(view is RecyclerView){
            val context = view.context
            view.layoutManager = LinearLayoutManager(context)
            view.adapter = adapter
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModelObservables()
    }

    override fun initViewModelObservables() {
        viewModel.itemsLD.observe(viewLifecycleOwner) {
            adapter.update(it)
        }

        viewModel.getCheckBoxVisibility().observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            if(it.status == State.Status.REMOVED){
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.clearCheckedItems()
        adapter.notifyDataSetChanged()
    }

    override fun initOnClickListeners() {
        TODO("Not yet implemented")
    }
}
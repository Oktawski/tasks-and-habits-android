package com.example.tah.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tah.R
import com.example.tah.databinding.FragmentTodosBinding
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import com.example.tah.viewModels.TodoViewModel

class TodosFragment: Fragment(R.layout.fragment_todos) {

    private val todoViewModel: TodoViewModel by viewModels()

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TodoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TodoRecyclerViewAdapter(mutableListOf(), todoViewModel)

        initAdapter()
        initOnClickListeners()
        initViewModelObservables()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModelObservables(){
        todoViewModel.itemsLD.observe(requireActivity()){
            adapter.update(it)
        }

        todoViewModel.state.observe(requireActivity()){
            with(binding) {
                when (it.status) {
                    State.Status.LOADING -> addIcon.visibility = View.GONE

                    State.Status.SUCCESS -> {
                        addText.setText("")
                        addIcon.visibility = View.VISIBLE
                    }

                    State.Status.ADDED -> {
                        addText.setText("")
                        addIcon.visibility = View.VISIBLE
                        todosRecyclerView.smoothScrollToPosition(todosRecyclerView.bottom)
                    }

                    else -> addIcon.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initOnClickListeners(){
        with(binding) {
            addIcon.setOnClickListener {
                val name: String = addText.text.toString()

                if (name.isNotEmpty()) {
                    todoViewModel.add(Todo(null, name = name, isComplete = false))
                } else {
                    addText.error = "Cannot be blank"
                }
            }

            textDeleteCompleted.setOnClickListener {
                todoViewModel.deleteSelected()
            }
        }
    }

    private fun initAdapter(){
        binding.todosRecyclerView.adapter = adapter
    }
}



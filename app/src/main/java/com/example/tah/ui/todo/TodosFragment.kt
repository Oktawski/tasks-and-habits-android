package com.example.tah.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.R
import com.example.tah.models.Todo
import com.example.tah.viewModels.TodoViewModel

class TodosFragment: Fragment(R.layout.fragment_todos) {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var addEditText: EditText
    private lateinit var addIcon: ImageButton
    private lateinit var deleteCompletedText: TextView
    private lateinit var recyclerViewTodos: RecyclerView
    private lateinit var adapter: TodoRecyclerViewAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addEditText = view.findViewById(R.id.add_text)
        addIcon = view.findViewById(R.id.add_icon)
        deleteCompletedText = view.findViewById(R.id.text_delete_completed)
        recyclerViewTodos = view.findViewById(R.id.todos_recycler_view)
        adapter = TodoRecyclerViewAdapter(emptyList(), requireContext(), todoViewModel)

        initAdapter()
        initOnClickListeners()
        initViewModelObservables()

    }

    private fun initViewModelObservables(){
        todoViewModel.itemsLD.observe(requireActivity()){
            adapter.update(it)
            recyclerViewTodos.scrollToPosition(it.size-1)
        }
    }

    private fun initOnClickListeners(){
        addIcon.setOnClickListener {
            val name: String = addEditText.text.toString()
            todoViewModel.add(Todo(null, name = name, isComplete = false))
        }

        deleteCompletedText.setOnClickListener {
            todoViewModel.deleteSelected()
        }
    }

    private fun initAdapter(){
        recyclerViewTodos.adapter = adapter
    }
}
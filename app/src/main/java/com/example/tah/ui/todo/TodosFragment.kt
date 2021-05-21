package com.example.tah.ui.todo

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tah.R
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import com.example.tah.viewModels.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections.emptyList

class TodosFragment: Fragment(R.layout.fragment_todos) {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var addEditText: EditText
    private lateinit var addIcon: FloatingActionButton
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
        }

        todoViewModel.state.observe(requireActivity()){
            when(it.status){
                State.Status.LOADING -> addIcon.visibility = View.GONE

                State.Status.SUCCESS -> {
                    addEditText.setText("")
                    addIcon.visibility = View.VISIBLE
                }

                State.Status.ADDED -> {
                    addEditText.setText("")
                    addIcon.visibility = View.VISIBLE
                    recyclerViewTodos.smoothScrollToPosition(recyclerViewTodos.bottom)
                }

                else -> addIcon.visibility  = View.VISIBLE
            }
        }
    }

    private fun initOnClickListeners(){
        addIcon.setOnClickListener {
            val name: String = addEditText.text.toString()

            if(name.isNotEmpty()) {
                todoViewModel.add(Todo(null, name = name, isComplete = false))
            }
            else{
                addEditText.error = "Cannot be blank"
            }
        }

        deleteCompletedText.setOnClickListener {
            todoViewModel.deleteSelected()
        }
    }

    private fun initAdapter(){
        recyclerViewTodos.adapter = adapter
    }
}



package com.example.tah.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.databinding.FragmentTodosBinding
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.models.Todo
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.TaskWithTodosViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodosAddFragment
    : Fragment(R.layout.fragment_todos),
    ViewInitializable
{

    val todos = mutableListOf<Todo>()

    private lateinit var taskWithTodosViewModel: TaskWithTodosViewModel

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var adapter: TodoRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        taskWithTodosViewModel =
            ViewModelProvider(requireActivity()).get(TaskWithTodosViewModel::class.java)
/*
        taskWithTodosViewModel.taskWithTodos.value =
            TaskWithTodos(Task(TaskType.SHOPPING), mutableListOf())*/

        _binding = FragmentTodosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initOnClickListeners()
        initViewModelObservables()
    }

    override fun initOnClickListeners() {
        with (binding) {
            addIcon.setOnClickListener {
                val name = addText.text.toString()
                Log.i("TodosAdd", "initOnClickListeners: ${name}")
                if (name.isNotEmpty()) {
                    taskWithTodosViewModel.addTodo(Todo(
                        todoId = null,
                        name = binding.addText.text.toString(),
                        isComplete = false,
                        taskId = null))
                    //adapter.differ.submitList(taskWithTodosViewModel.taskWithTodos.value?.todos)
                } else {
                    addText.error = "Cannot be blank"
                }
            }
        }
    }

    override fun initViewModelObservables() {
        taskWithTodosViewModel.taskWithTodos.observe(viewLifecycleOwner) {
            adapter.differ.submitList(it.todos)
        }
    }

    private fun initAdapter() {
        binding.todosRecyclerView.adapter = adapter
    }


}
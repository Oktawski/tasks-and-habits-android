package com.example.tah.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.databinding.FragmentTodosBinding
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
        _binding = FragmentTodosBinding.inflate(inflater, container, false)
        taskWithTodosViewModel =
            ViewModelProvider(this).get(TaskWithTodosViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todosRecyclerView.adapter = adapter
        initViewModelObservables()
        initOnClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initOnClickListeners() {
        with (binding) {
            addIcon.setOnClickListener {
                val name = addText.text.toString()
                if (name.isNotEmpty()) {
                    taskWithTodosViewModel.addTodo(Todo(
                        todoId = null,
                        name = name,
                        isComplete = false,
                        taskId = null))
                } else {
                    addText.error = "Cannot be blank"
                }
            }
        }
    }

    override fun initViewModelObservables() {
    }

}

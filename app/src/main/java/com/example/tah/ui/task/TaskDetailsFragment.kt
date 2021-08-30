package com.example.tah.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tah.R
import com.example.tah.databinding.DetailsTaskBinding
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.models.Todo
import com.example.tah.ui.todo.TodosFragment
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TaskWithTodosViewModel
import com.example.tah.viewModels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailsFragment: Fragment(R.layout.details_task), ViewInitializable {

    private var _binding: DetailsTaskBinding? = null
    private val binding get() = _binding!!

    private var taskId: Int? = null
    private var task: Task? = null
    private var todos: List<Todo>? = null

    private val taskViewModel: TaskViewModel by viewModels()
    private val todoViewModel: TodoViewModel by viewModels()
    private val taskWithTodosViewModel: TaskWithTodosViewModel by viewModels()

    companion object {
        fun newInstance(taskId: Int): TaskDetailsFragment{
            val fragment = TaskDetailsFragment()
            val args = Bundle()
            args.putInt("taskId", taskId)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskId = arguments?.getInt("taskId")
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = DetailsTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClickListeners()
        initViewModelObservables()


        CoroutineScope(Dispatchers.Main).launch {
            task = taskViewModel.getTaskById(taskId!!)
            todos = todoViewModel.getTodosByTaskId(taskId!!)

            binding.name.setText(task?.name)
            binding.description.setText(task?.description)

            taskWithTodosViewModel.taskWithTodos.value =
                TaskWithTodos(task!!, todos!!.toMutableList())

            binding.name.setText(task?.name)
            if(task?.type == TaskType.SHOPPING) {
                binding.descriptionLayout.visibility = View.GONE
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, TodosFragment.newInstance(taskId!!))
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initOnClickListeners() {
        binding.saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                task = taskViewModel.getTaskById(taskId!!)
                task?.name = binding.name.text.toString()
                task?.description = binding.description.text.toString()
                taskViewModel.update(task!!)
            }
        }

        binding.deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val task = taskViewModel.getTaskById(taskId!!)
                taskViewModel.delete(task)
            }
        }
    }

    override fun initViewModelObservables() {
        taskViewModel.state.observe(viewLifecycleOwner){
            when(it.status){
                State.Status.REMOVED -> {
                    requireActivity().finish()
                }
                State.Status.UPDATED -> {
                    requireActivity().finish()
                }
            }
            Log.i("STATUSMSG", "${it.message}")
            toast(it.message)
        }

        todoViewModel.itemsLD?.observe(viewLifecycleOwner) {
            Log.i("TODOSIZE", "initViewModelObservables: ${it.size}")
            taskWithTodosViewModel.taskWithTodos.value = TaskWithTodos(task!!, it.toMutableList())
        }
    }

    private fun toast(message: String?){
        if(!message.isNullOrEmpty()){
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
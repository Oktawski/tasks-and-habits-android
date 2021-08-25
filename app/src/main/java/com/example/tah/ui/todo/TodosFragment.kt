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
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class TodosFragment: Fragment(R.layout.fragment_todos) {

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Int? = null
    private var task: Task? = null

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var adapter: TodoRecyclerViewAdapter

    companion object {
        fun newInstance(taskId: Int): TodosFragment {
            val args = Bundle()
            args.putInt("taskId", taskId)
            val fragment = TodosFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments?.getInt("taskId") != -1) {
            taskId = arguments?.getInt("taskId")
        } else {
            task = Task( "shoppingname", "Shopping", TaskType.SHOPPING, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(inflater, container, false)
        todoViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        CoroutineScope(Dispatchers.Main).launch {
            if (taskId == null) taskId = taskViewModel.addGetId(task!!).toInt()
            todoViewModel.getAllByTaskId(taskId!!)
            initOnClickListeners()
            initViewModelObservables()
        }
    }

    override fun onResume() {
        super.onResume()
        todoViewModel.getCompletedList()
    }

    override fun onPause() {
        super.onPause()
        //(activity as MainActivity).setDeleteIconVisibility(View.GONE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModelObservables(){
        todoViewModel.completedTodos.observe(requireActivity()) {
            /*if(it.isNotEmpty()) (activity as MainActivity).setDeleteIconVisibility(View.VISIBLE)
            else (activity as MainActivity).setDeleteIconVisibility(View.GONE)*/
        }

        todoViewModel.itemsLD!!.observe(requireActivity()){
            Log.i("SIZE", it.size.toString())
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
                        todoViewModel.getAllByTaskId(taskId!!)
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
                    todoViewModel.add(
                        Todo(null, name = name, isComplete = false, taskId = taskId)
                    )
                    Log.i("Adding Todo", "Task Id: $taskId")

                } else {
                    addText.error = "Cannot be blank"
                }
            }
        }
    }

    private fun initAdapter(){
        binding.todosRecyclerView.adapter = adapter
    }
}



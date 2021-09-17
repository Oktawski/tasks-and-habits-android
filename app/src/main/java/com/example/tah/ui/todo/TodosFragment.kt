package com.example.tah.ui.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.databinding.FragmentTodosBinding
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.models.Todo
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.utilities.State
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TaskWithTodosViewModel
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
    private lateinit var taskWithTodosViewModel: TaskWithTodosViewModel
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTodosBinding.inflate(inflater, container, false)
        todoViewModel = ViewModelProvider(requireActivity()).get(TodoViewModel::class.java)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        taskWithTodosViewModel =
            ViewModelProvider(requireActivity()).get(TaskWithTodosViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        CoroutineScope(Dispatchers.Main).launch {
            if (arguments?.getInt("taskId") != -1) {
                taskId = arguments?.getInt("taskId")
                task = taskViewModel.getTaskById(taskId!!)
            } else {
                task = Task("", "", TaskType.SHOPPING, false)
                //remove observers
                taskId = taskViewModel.add(task!!).toInt()
            }

            if(taskId != null) todoViewModel.getAllByTaskId(taskId!!)

            initOnClickListeners()
            initViewModelObservables()
        }
    }

    override fun onResume() {
        super.onResume()
        if(taskId != null) todoViewModel.getCompletedByTaskId(taskId!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModelObservables(){
        todoViewModel.getCompletedByTaskId(taskId!!).observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) (activity as AddAndDetailsActivity).setDeleteIconVisibility(View.VISIBLE)
            else (activity as AddAndDetailsActivity).setDeleteIconVisibility(View.GONE)
        }

        todoViewModel.itemsLD!!.observe(requireActivity()){
            adapter.differ.submitList(it)
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
        activity?.findViewById<ImageView>(R.id.delete_icon)?.setOnClickListener {
            todoViewModel.deleteCompletedByTaskId(taskId!!)
        }

        with(binding) {
            addIcon.setOnClickListener {
                val name: String = addText.text.toString()

                if (name.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        todoViewModel.add(
                            Todo(null, name, false, taskId)
                        )
                    }
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



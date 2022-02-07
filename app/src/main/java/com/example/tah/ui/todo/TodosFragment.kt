package com.example.tah.ui.todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.databinding.FragmentTodosBinding
import com.example.tah.models.Task
import com.example.tah.models.Todo
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.utilities.State
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TodosFragment
    : Fragment(R.layout.fragment_todos)
{
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var taskViewModel: TaskViewModel
    private var taskId: Long? = null
    private var task: Task? = null

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var adapter: TodoRecyclerViewAdapter

    companion object {
        fun newInstance(taskId: Long): TodosFragment {
            val args = Bundle()
            args.putLong("taskId", taskId)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        CoroutineScope(Dispatchers.Main).launch {
            taskId = arguments?.getLong("taskId", -1L)
            task = taskViewModel.getTaskById(taskId!!)
            todoViewModel.getTodosByTaskId(taskId!!)

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

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModelObservables(){
        todoViewModel.getCompletedByTaskId(taskId!!).observe(viewLifecycleOwner) {
            if(it.isNotEmpty())
                    (activity as AddAndDetailsActivity).setDeleteIconVisibility(View.VISIBLE)
            else
                    (activity as AddAndDetailsActivity).setDeleteIconVisibility(View.GONE)
        }

        todoViewModel.itemsLD!!.observe(requireActivity()){
            adapter.differ.submitList(it)

            // Without notifying about whole data set the UI is not always updating the icon
            // If anyone knows better way I would appreciate if you told me <3
            adapter.notifyDataSetChanged()
        }

        todoViewModel.state.observe(requireActivity()){
            with(binding) {
                when (it.status) {
                    State.Status.LOADING -> addIcon.visibility = View.GONE
                    State.Status.ADDED -> {
                        addText.setText("")
                        addIcon.visibility = View.VISIBLE
                        todoViewModel.getTodosByTaskId(taskId!!)
                        todosRecyclerView.smoothScrollToPosition(todosRecyclerView.bottom)
                    }
                    else -> {
                        addText.setText("")
                        addIcon.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun initOnClickListeners(){
        activity?.findViewById<ImageView>(R.id.delete_icon)?.setOnClickListener {
            todoViewModel.deleteCompletedByTaskId(taskId!!)
        }

        binding.addIcon.setOnClickListener {
            val name: String = binding.addText.text.toString()

            if (name.isNotEmpty()) {
                todoViewModel.add(Todo(null, name, false, taskId))
            } else {
                binding.addText.error = "Cannot be blank"
            }
        }
    }

    private fun initAdapter(){
        binding.todosRecyclerView.adapter = adapter
    }
}



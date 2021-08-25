package com.example.tah.ui.task

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tah.R
import com.example.tah.databinding.DetailsTaskBinding
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.ui.todo.TodosFragment
import com.example.tah.utilities.Converters
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class TaskDetailsFragment: Fragment(R.layout.details_task), ViewInitializable {

    private var _binding: DetailsTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private var taskId: Int? = null
    private var task: Task? = null

    private val taskViewModel: TaskViewModel by viewModels()
    private val todoViewModel: TodoViewModel by viewModels()

    companion object {
        fun newInstance(name: String?, description: String?): TaskDetailsFragment{
            val fragment = TaskDetailsFragment()
            val args = Bundle()
            args.putString("name", name)
            args.putString("description", description)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        taskId = requireActivity().intent.getIntExtra("taskId", -1);
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.details_task, container, false)
        _binding = DetailsTaskBinding.inflate(inflater, container, false)
        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        saveButton = view.findViewById(R.id.save_button)
        deleteButton = view.findViewById(R.id.delete_button)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name.setText(arguments?.getString("name"))
        description.setText(arguments?.getString("description"))

        initOnClickListeners()
        initViewModelObservables()


        CoroutineScope(Dispatchers.Main).launch {
            task = taskViewModel.getTaskById(taskId!!)
            todoViewModel.getAllByTaskId(taskId!!)
            binding.name.setText(task?.name)
            Log.i("ISShopping", "onViewCreated: ${task?.type == TaskType.SHOPPING}")
            if(task?.type == TaskType.SHOPPING) {
                binding.descriptionLayout.visibility = View.GONE
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, TodosFragment.newInstance(taskId!!))
                    .commit()
            }
        }
    }

    override fun initOnClickListeners() {
        binding.saveButton.setOnClickListener {
            taskViewModel.getById(taskId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        it.name = binding.name.text.toString()
                        it.description = binding.description?.text.toString()
                        taskViewModel.update(it)
                    },{})
        }

        binding.deleteButton.setOnClickListener {
            taskViewModel.getById(taskId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({taskViewModel.delete(it)},
                            {toast("Something went wrong")})
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
            toast(it.message)
        }

        todoViewModel.itemsLD?.observe(viewLifecycleOwner) {
            Log.i("TODOSIZE", "initViewModelObservables: ${it.size}")

        }
    }

    private fun toast(message: String?){
        if(!message.isNullOrEmpty()){
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.databinding.AddTaskFragmentBinding
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.ui.todo.TodosAddFragment
import com.example.tah.ui.todo.TodosFragment
import com.example.tah.utilities.Converters
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewHelper
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TaskWithTodosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class TaskAddFragment
    : Fragment(R.layout.add_task_fragment),
    ViewInitializable,
    ViewHelper
{
    private var _binding: AddTaskFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        val adapter = ArrayAdapter(
            requireActivity(), android.R.layout.simple_spinner_dropdown_item, TaskType.values()
        )

        _binding = AddTaskFragmentBinding.inflate(inflater, container, false)
        (binding.typeLayout.editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
            onItemClickListener = spinnerAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AddAndDetailsActivity).setTitle("New Task")
        initOnClickListeners()
        initViewModelObservables()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initOnClickListeners() {
        binding.add.setOnClickListener{
            val nameText = binding.name.text.toString()
            val descriptionText = binding.description.text.toString()
            val type = binding.typeLayout.editText?.text.toString()

            if(nameText.isNotEmpty() && type.isNotEmpty()){
                if (Converters.toType(type) == TaskType.SHOPPING) {
                    CoroutineScope(Dispatchers.Main).launch {
                        /*viewModel.addTaskWithTodos(
                            taskWithTodosViewModel.taskWithTodos.value!!
                        )*/
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.add(
                            Task(nameText, descriptionText, Converters.toType(type), false)
                        )
                    }
                }
            }
            else{
                showErrorMessages(binding.name, binding.typeLayout)
            }
        }
    }

    override fun initViewModelObservables() {
        viewModel.state.observe(viewLifecycleOwner){
            when(it.status){
                State.Status.LOADING -> viewsLoading()
                State.Status.SUCCESS -> viewsNotLoading()
                State.Status.ADDED -> requireActivity().finish()
                else -> viewsNotLoading()
            }
            toast(it.message)
        }
    }

    private fun viewsLoading(){
        binding.add.hide()
        // show progressBar
    }

    private fun viewsNotLoading(){
        binding.add.show()
        // hide progressBar
    }

    private fun toast(message: String?){
        if(!message.isNullOrEmpty()){
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private val spinnerAdapter: AdapterView.OnItemClickListener
    = AdapterView.OnItemClickListener { _, _, position, _ ->
        if (position == 2) {
            binding.descriptionLayout.visibility = View.GONE
            viewModel.state.removeObservers(viewLifecycleOwner)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TodosAddFragment())
                .addToBackStack("todoFragment")
                .commit()
        } else {
            initViewModelObservables()
            binding.descriptionLayout.visibility = View.VISIBLE
            parentFragmentManager.popBackStack("todoFragment", -1)
        }
    }
}
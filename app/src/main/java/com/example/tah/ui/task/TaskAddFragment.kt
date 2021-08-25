package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.databinding.AddTaskFragmentBinding
import com.example.tah.utilities.ViewInitializable
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.ui.todo.TodosFragment
import com.example.tah.utilities.Converters
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewHelper
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TaskAddFragment
    : Fragment(R.layout.add_task_fragment),
    ViewInitializable,
    ViewHelper
{
    private var _binding: AddTaskFragmentBinding? = null
    private val binding get() = _binding!!
    //private val viewModel: TaskViewModel by viewModels()
    private lateinit var viewModel: TaskViewModel
    private val taskTypes = TaskType.values()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
        _binding = AddTaskFragmentBinding.inflate(inflater, container, false)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_spinner_dropdown_item, taskTypes)
        (binding.typeLayout.editText as? AutoCompleteTextView)?.setAdapter(adapter)

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
                //viewModel.add(Task(nameText, descriptionText, Converters.toType(type), false))

                if(Converters.toType(type) == TaskType.SHOPPING) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TodosFragment())
                        .commit()
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
                State.Status.SUCCESS -> {
                    viewsNotLoading()
                }
                //State.Status.ADDED -> requireActivity().finish()
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
}
package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tah.R
import com.example.tah.databinding.DetailsTaskBinding
import com.example.tah.models.TaskType
import com.example.tah.ui.todo.TodosFragment
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.TaskDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailsFragment: Fragment(R.layout.details_task), ViewInitializable {

    private var _binding: DetailsTaskBinding? = null
    private val binding get() = _binding!!

    private val taskDetailsViewModel: TaskDetailsViewModel by viewModels()

    companion object {
        fun newInstance(taskId: Int): TaskDetailsFragment{
            val fragment = TaskDetailsFragment()
            val args = Bundle()
            args.putInt("taskId", taskId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = DetailsTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailsViewModel.init(arguments?.getInt("taskId")!!)
        initOnClickListeners()
        initViewModelObservables()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initOnClickListeners() {
        binding.saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                taskDetailsViewModel.task.value.apply {
                    this?.name = binding.name.text.toString()
                    this?.description = binding.description.text.toString()
                }
                taskDetailsViewModel.update()
            }
        }

        binding.deleteButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                taskDetailsViewModel.delete()
            }
        }
    }

    override fun initViewModelObservables() {
        taskDetailsViewModel.task.observe(viewLifecycleOwner) {
            binding.name.setText(it.name)
            binding.description.setText(it.description)

            val task = it

            CoroutineScope(Dispatchers.Main).launch {
                binding.name.setText(task.name)
                binding.description.setText(task.description)

                if(task.type == TaskType.SHOPPING) {
                    binding.descriptionLayout.visibility = View.GONE
                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                            TodosFragment.newInstance(task.taskId!!))
                        .commit()
                }
            }
        }

        taskDetailsViewModel.state.observe(viewLifecycleOwner) {
            when(it.status){
                State.Status.REMOVED -> {
                    requireActivity().finish()
                }
                State.Status.UPDATED -> {
                    requireActivity().finish()
                }
                else -> TODO()
            }
            toast(it.message)
        }
    }

    private fun toast(message: String?){
        if(!message.isNullOrEmpty()){
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
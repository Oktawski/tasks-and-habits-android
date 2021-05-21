package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.utilities.ViewInits
import com.example.tah.models.Task
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.utilities.State
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class TaskAddFragment: Fragment(R.layout.add_task_fragment), ViewInits {

    private lateinit var viewModel: TaskViewModel
    private lateinit var name: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var addFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.add_task_fragment, container, false)
        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        addFab = view.findViewById(R.id.add);

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClickListeners()
        initViewModelObservables()
    }

    override fun initOnClickListeners() {
        addFab.setOnClickListener{
            val nameText = name.text.toString()
            val descriptionText = description.text.toString()

            if(nameText.isNotEmpty()){
                viewModel.add(Task(nameText, descriptionText, false))
            }
            else{
                name.error = "Cannot be blank"
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
                State.Status.ADDED -> activity?.finish()
                else -> viewsNotLoading()
            }
            toast(it.message)
        }
    }

    private fun viewsLoading(){
        addFab.hide()
        // show progressBar
    }

    private fun viewsNotLoading(){
        addFab.show()
        // hide progressBar
    }

    private fun toast(message: String?){
        if(!message.isNullOrEmpty()){
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
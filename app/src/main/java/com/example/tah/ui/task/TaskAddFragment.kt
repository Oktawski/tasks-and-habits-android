package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.models.Task
import com.example.tah.models.TaskViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskAddFragment: Fragment(R.layout.add_task_fragment){

    private lateinit var viewModel: TaskViewModel
    private lateinit var nameET: EditText
    private lateinit var descriptionET: EditText
    private lateinit var addFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.add_task_fragment, container, false)
        nameET = view.findViewById(R.id.name)
        descriptionET = view.findViewById(R.id.description)
        addFab = view.findViewById(R.id.add);

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addFab.setOnClickListener{
            val name = nameET.text.toString()
            val description = descriptionET.text.toString()

            viewModel.add(Task(name, description, false))
        }
    }
}
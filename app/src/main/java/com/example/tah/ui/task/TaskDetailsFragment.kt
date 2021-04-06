package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.ViewInits
import com.example.tah.models.TaskViewModel

class TaskDetailsFragment: Fragment(R.layout.details_task), ViewInits {

    private lateinit var viewModel: TaskViewModel
    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.details_task, container, false)
        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun initOnClickListeners() {
        saveButton.setOnClickListener {
            TODO("Not yet implemented")
        }

        deleteButton.setOnClickListener {
            TODO("Not yet implemented")
        }
    }

    override fun initViewModelObservables() {
        TODO("Not yet implemented")
    }
}
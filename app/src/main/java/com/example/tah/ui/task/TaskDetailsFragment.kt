package com.example.tah.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.ViewInits
import com.example.tah.models.TaskViewModel
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskDetailsFragment: Fragment(R.layout.details_task), ViewInits {

    private lateinit var viewModel: TaskViewModel
    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private var taskId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        taskId = requireActivity().intent.getIntExtra("taskId", -1);
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.details_task, container, false)
        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        saveButton = view.findViewById(R.id.save_button)
        deleteButton = view.findViewById(R.id.delete_button)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClickListeners()
        initViewModelObservables()
    }

    override fun initOnClickListeners() {
        saveButton.setOnClickListener {
            toast("Not yet implemented")
        }

        deleteButton.setOnClickListener {
            viewModel.getById(taskId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({viewModel.delete(it)},
                            {toast("Something went wrong")})
        }
    }

    override fun initViewModelObservables() {
        viewModel.state.observe(viewLifecycleOwner){
            when(it.status){
                State.Status.REMOVED -> requireActivity().finish()
                else -> toast(it.message)
            }
        }
    }

    private fun toast(message: String?){
        if(!message.isNullOrEmpty()){
            Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
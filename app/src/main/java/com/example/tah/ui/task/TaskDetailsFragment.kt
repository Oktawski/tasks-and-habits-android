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
import com.example.tah.models.Task
import com.example.tah.models.TaskViewModel
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskDetailsFragment: Fragment(R.layout.details_task), ViewInits {

    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private var taskId: Int? = null

    private lateinit var taskViewModel: TaskViewModel

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
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

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

        name.setText(arguments?.getString("name"))
        description.setText(arguments?.getString("description"))

        initOnClickListeners()
        initViewModelObservables()
    }

    override fun initOnClickListeners() {
        saveButton.setOnClickListener {
            taskViewModel.getById(taskId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        it.name = name.text.toString()
                        it.description = description.text.toString()
                        taskViewModel.update(it)
                    },{})
        }

        deleteButton.setOnClickListener {
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
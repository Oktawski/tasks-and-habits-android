package com.example.tah.ui.habit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.models.Habit
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewHelper
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.HabitViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HabitAddFragment
    : Fragment(R.layout.add_habit_fragment), ViewInitializable, ViewHelper {

    private lateinit var viewModel: HabitViewModel
    private lateinit var name: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var hoursInput: TextInputLayout
    private lateinit var minutesInput: TextInputLayout
    private lateinit var fabAdd: FloatingActionButton

    private val hours = Array(10){it}
    private val minutes = Array(60){it}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(HabitViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.add_habit_fragment, container, false)

        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        hoursInput = view.findViewById(R.id.hours_layout)
        minutesInput = view.findViewById(R.id.minutes_layout)
        fabAdd = view.findViewById(R.id.fab_add)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOnClickListeners()
        initViewModelObservables()
        initSpinnerAdapters()
    }

    override fun initOnClickListeners(){

        fabAdd.setOnClickListener {
            if(name.text.isNullOrEmpty()){
                showErrorMessages(name)
            }
            else {
                val habit = Habit.new(
                    name.text.toString(), 
                    description.text.toString(), 
                    hoursInput.editText?.text.toString().toIntOrNull() ?: 0,
                    minutesInput.editText?.text.toString().toIntOrNull() ?: 0)

                if(habit.sessionLength == 0L) Toast.makeText(
                    requireActivity(),
                    "Session length cannot be less than 1 minute",
                    Toast.LENGTH_SHORT
                ).show()

                else viewModel.add(habit)
            }
        }

    }

    override fun initViewModelObservables() {
        viewModel.state.observe(viewLifecycleOwner){
            when(it.status){
                State.Status.ADDED -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
            }
        }
    }

    private fun initSpinnerAdapters(){
        createSpinnerAdapter(minutesInput, minutes)
        createSpinnerAdapter(hoursInput, hours)
    }

    private fun createSpinnerAdapter(layout: TextInputLayout, array: Array<Int>){
        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            array
        ).also { adapter ->
            (layout.editText as AutoCompleteTextView).setAdapter(adapter)
        }
    }
}
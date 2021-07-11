package com.example.tah.ui.habit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.databinding.AddHabitFragmentBinding
import com.example.tah.models.Habit
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewHelper
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.HabitViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class HabitAddFragment
: Fragment(R.layout.add_habit_fragment),
    ViewInitializable,
    ViewHelper {

    private var _binding: AddHabitFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HabitViewModel by viewModels()
    private val hours = Array(10){it}
    private val minutes = Array(60){it}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = AddHabitFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AddAndDetailsActivity).setTitle("Add Habit")
        initOnClickListeners()
        initViewModelObservables()
        initSpinnerAdapters()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initOnClickListeners(){
        with(binding) {
            fabAdd.setOnClickListener {
                if (name.text.isNullOrEmpty()) {
                    showErrorMessages(name)
                } else {
                    val habit = Habit.new(
                        name.text.toString(),
                        description.text.toString(),
                        hoursLayout.editText?.text.toString().toIntOrNull() ?: 0,
                        minutesLayout.editText?.text.toString().toIntOrNull() ?: 0
                    )

                    if (habit.sessionLength == 0L) Toast.makeText(
                        requireActivity(),
                        "Session length cannot be less than 1 minute",
                        Toast.LENGTH_SHORT
                    ).show()
                    else viewModel.add(habit)
                }
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
        createSpinnerAdapter(binding.minutesLayout, minutes)
        createSpinnerAdapter(binding.hoursLayout, hours)
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
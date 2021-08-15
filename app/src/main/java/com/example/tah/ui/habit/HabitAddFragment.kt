package com.example.tah.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tah.R
import com.example.tah.databinding.AddHabitFragmentBinding
import com.example.tah.models.Habit
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewHelper
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.HabitViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HabitAddFragment
: Fragment(R.layout.add_habit_fragment),
    ViewInitializable,
    ViewHelper
{
    private var _binding: AddHabitFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = AddHabitFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AddAndDetailsActivity).setTitle("New Habit")
        initTimePicker()
        initOnClickListeners()
        initViewModelObservables()
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
                        timePicker.hour,
                        timePicker.minute
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

    private fun initTimePicker() {
        binding.timePicker.setIs24HourView(true)
        binding.timePicker.hour = 0
        binding.timePicker.minute = 1
    }

}

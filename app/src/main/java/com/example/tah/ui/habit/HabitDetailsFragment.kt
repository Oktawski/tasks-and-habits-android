package com.example.tah.ui.habit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tah.R
import com.example.tah.databinding.DetailsHabitBinding
import com.example.tah.models.Habit
import com.example.tah.ui.animations.ViewAnimations
import com.example.tah.utilities.State
import com.example.tah.utilities.ViewHabitTime
import com.example.tah.utilities.ViewHelper
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class HabitDetailsFragment
: Fragment(R.layout.details_habit),
    ViewInitializable,
    ViewHabitTime,
    ViewHelper
{
    private var _binding: DetailsHabitBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitViewModel by viewModels()
    private var job: Job? = null

    private lateinit var habit: Habit
    private var habitId: Long? = -1L
    private var sessionLength: Long? = null

    companion object{
        fun newInstance(id: Long): HabitDetailsFragment {
            val fragment = HabitDetailsFragment()
            val args = Bundle()
            args.putLong("habitId", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        habitId = arguments?.getLong("habitId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsHabitBinding.inflate(inflater, container, false)
        binding.timePicker.setIs24HourView(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancelSaveLayout.visibility = View.GONE
        getHabit()
        initOnClickListeners()
        initViewModelObservables()
        toggleEditText(binding.name, binding.description)
    }

    override fun onResume() {
        super.onResume()
        setEditableView(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }

    override fun initOnClickListeners() {
        with(binding) {
            editButton.setOnClickListener { viewModel.setEditable(true) }
            cancelButton.setOnClickListener { viewModel.setEditable(false) }
            deleteButton.setOnClickListener { delete() }
            saveButton.setOnClickListener { save() }
            fabStart.setOnClickListener { startHabitStartedFragment() }
        }
    }

    override fun initViewModelObservables() {
        viewModel.editable.observe(viewLifecycleOwner) { setEditableView(it) }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it.status) {
                State.Status.REMOVED -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
                State.Status.UPDATED -> {
                    setEditableView(false)
                    job = CoroutineScope(Dispatchers.Main).launch {
                        val habit = viewModel.getByIdSus(habitId)
                        sessionLength = habit.sessionLength
                        inflateViews(habit)
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> Toast.makeText(activity, "Something else", Toast.LENGTH_SHORT).show()
            }

            Log.i("STATE", "initViewModelObservables: ${it.message}")
        }
    }

    private fun getHabit() {
        job = CoroutineScope(Dispatchers.Main).launch {
            habit = viewModel.getByIdSus(habitId)
            inflateViews(habit)
        }
    }

    private fun inflateViews(habit: Habit) {
        with (binding) {
            name.setText(habit.name)
            description.setText(habit.description)
            val timeMap = getTimeStrings(habit.sessionLength)
            timePicker.hour = timeMap["Hours"]?.toInt()!!
            timePicker.minute = timeMap["Minutes"]?.toInt()!!
        }
    }

    private fun save() {
        val sessionLengthInSec =
            (binding.timePicker.hour * 60 * 60 + binding.timePicker.minute * 60).toLong()

        habit.apply {
            this.name = binding.name.text.toString()
            this.description = binding.description.text.toString()
            this.sessionLength = sessionLengthInSec
        }

        viewModel.update(habit)
    }

    private fun delete() {
        job = CoroutineScope(Dispatchers.Main).launch {
            val habit = viewModel.getByIdSus(habitId)
            viewModel.delete(habit)
        }
    }

    private fun startHabitStartedFragment() {
        val fragment = HabitStartedFragment.newInstance(habitId!!)
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.add_fragment_container, fragment, "habitStartedFragment")
            .addToBackStack("habitStartedFragment")
            .commit()
    }

    private fun setEditableView(isEditable: Boolean) {
        with (binding) {
            ViewAnimations.hide(if (isEditable) deleteEditLayout else cancelSaveLayout)
            ViewAnimations.show(if (isEditable) cancelSaveLayout else deleteEditLayout)
            toggleEditText(isEditable, name, description)
            timePicker.isEnabled = isEditable
        }
    }

}

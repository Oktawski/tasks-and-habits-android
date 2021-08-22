package com.example.tah.ui.habit

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


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

    private lateinit var mainHandler: Handler

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
        mainHandler = Handler(Looper.getMainLooper())
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
        with(binding) {
            toggleEditText(name, description)
        }
    }

    override fun onResume() {
        super.onResume()
        setNotEditableView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job?.cancel()
    }

    override fun initOnClickListeners() {
        with(binding) {
            editButton.setOnClickListener { viewModel.editable.value = true }
            cancelButton.setOnClickListener { viewModel.editable.value = false }

            deleteButton.setOnClickListener {
                job = CoroutineScope(Dispatchers.Main).launch {
                    val habit = viewModel.getByIdSus(habitId)
                    viewModel.delete(habit)
                }
            }

            saveButton.setOnClickListener {
                val hours = timePicker.hour
                val minutes = timePicker.minute
                val sessionLengthInSec = (hours * 60 * 60 + minutes * 60).toLong()

                job = CoroutineScope(Dispatchers.IO).launch {
                    val habit = viewModel.getByIdSus(habitId)
                    habit.name = name.text.toString()
                    habit.description = description.text.toString()
                    habit.sessionLength = sessionLengthInSec
                    viewModel.update(habit)
                }
            }

            fabStart.setOnClickListener {
                val fragment = HabitStartedFragment.newInstance(habitId!!)
                requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out )
                    .replace(R.id.add_fragment_container, fragment, "habitStartedFragment")
                    .addToBackStack("habitStartedFragment")
                    .commit()
            }
        }
    }

    override fun initViewModelObservables() {
        viewModel.editable.observe(viewLifecycleOwner) {
            if(it) setEditableView() else setNotEditableView()
        }

        viewModel.state.observe(viewLifecycleOwner) {
            if(it.status == State.Status.REMOVED) {
                Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
            else if(it.status == State.Status.UPDATED) {
                setNotEditableView()
                job = CoroutineScope(Dispatchers.Main).launch {
                    val habit = viewModel.getByIdSus(habitId)
                    inflateViews(habit)
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    this@HabitDetailsFragment.sessionLength = habit.sessionLength
                }
            }
        }
    }

    private fun getHabit() {
        job = CoroutineScope(Dispatchers.Main).launch {
            inflateViews(viewModel.getByIdSus(habitId))
        }
    }

    private fun inflateViews(it: Habit) {
        with (binding) {
            name.setText(it.name)
            description.setText(it.description)
            val timeMap = getTimeStrings(it.sessionLength)
            timePicker.hour = timeMap["Hours"]?.toInt()!!
            timePicker.minute = timeMap["Minutes"]?.toInt()!!
        }
    }

    private fun setEditableView() {
        with(binding) {
            ViewAnimations.hide(binding.deleteEditLayout)
            ViewAnimations.show(binding.cancelSaveLayout)
            toggleEditText(name, description)
            timePicker.isEnabled = true
        }
    }

    private fun setNotEditableView() {
        with(binding) {
            ViewAnimations.hide(binding.cancelSaveLayout)
            ViewAnimations.show(binding.deleteEditLayout)
            toggleEditText(name, description)
            timePicker.isEnabled = false
        }
    }

}

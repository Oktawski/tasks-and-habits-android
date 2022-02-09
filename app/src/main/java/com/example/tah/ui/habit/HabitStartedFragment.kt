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
import com.example.tah.databinding.DetailsHabitStartedBinding
import com.example.tah.ui.main.AddAndDetailsActivity
import com.example.tah.utilities.TimeConverter
import com.example.tah.utilities.ViewInitializable
import com.example.tah.viewModels.HabitStartedViewModel
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job


@AndroidEntryPoint
class HabitStartedFragment
    : Fragment(R.layout.details_habit_started),
    TimeConverter,
    ViewInitializable
{
    private var _binding: DetailsHabitStartedBinding? = null
    private val binding get() = _binding!!

    private var habitId: Long? = null
    private var sessionLength = 0L
    private lateinit var mainHandler: Handler
    private var job: Job? = null

    private val viewModel: HabitStartedViewModel by viewModels()

    companion object {
        fun newInstance(id: Long): HabitStartedFragment {
            val fragment = HabitStartedFragment()
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
        _binding = DetailsHabitStartedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (habitId == null) {
            Toast.makeText(requireActivity(), "Habit not found", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.popBackStack()
        }

        activity?.findViewById<AppBarLayout>(R.id.app_bar)?.visibility = View.GONE
        (activity as AddAndDetailsActivity).setTitle("")
        getHabit(habitId!!)
        initViewModelObservables()
        initOnClickListeners()
        viewModel.startStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mainHandler.removeCallbacks(decreaseTime)
        job?.cancel()
        updateHabit(sessionLength)
    }

    override fun initViewModelObservables() {
        viewModel.habit.observe(viewLifecycleOwner) {
            sessionLength = it.sessionLength
            binding.habitName.text = it.name
            setRemainingTimeText()
        }

        viewModel.isStarted.observe(viewLifecycleOwner) { isStarted ->
            if(isStarted) {
                setRemainingTimeText()
                mainHandler.postDelayed(decreaseTime, 1)
            } else {
                mainHandler.removeCallbacks(decreaseTime)
                updateHabit(sessionLength)
            }
        }
    }

    override fun initOnClickListeners() {
        binding.fabStarted.setOnClickListener { viewModel.startStop() }
    }

    private fun getHabit(id: Long) = viewModel.getById(id)

    private fun updateHabit(sessionLength: Long) = viewModel.updateSessionLength(sessionLength)

    private fun setRemainingTimeText() {
        val timeMap = getTimeUnitsToValuesAsStrings(sessionLength)
        val timeText = "${timeMap["Hours"]}:${timeMap["Minutes"]}:${timeMap["Seconds"]}"
        binding.timeText.text = timeText
    }

    private val decreaseTime = object : Runnable {
        override fun run() {
            setRemainingTimeText()
            if (sessionLength > 0) {
                sessionLength--
            } else {
                viewModel.startStop()
                Toast.makeText(activity, "Time's up", Toast.LENGTH_SHORT).show()
            }
            mainHandler.postDelayed(this, 1000)
        }
    }
}
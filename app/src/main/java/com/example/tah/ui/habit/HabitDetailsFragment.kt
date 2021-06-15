package com.example.tah.ui.habit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.models.Habit
import com.example.tah.utilities.ViewHabitTime
import com.example.tah.utilities.ViewInits
import com.example.tah.viewModels.HabitViewModel
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HabitDetailsFragment: Fragment(R.layout.details_habit), ViewInits, ViewHabitTime {

    private lateinit var viewModel: HabitViewModel
    private lateinit var name: EditText
    private lateinit var description: EditText
    private lateinit var hoursInput: TextInputLayout
    private lateinit var minutesInput: TextInputLayout

    private var habitId: Long? = -1L

    private val hours = Array(10){it}
    private val minutes = Array(60){it}

    companion object{
        fun newInstance(id: Long): HabitDetailsFragment {
            val fragment = HabitDetailsFragment()
            val args = Bundle()
            args.putLong("habitId", id)
            fragment.arguments = args
            return fragment
        }
    }

    private val TAG = "DETAILS FRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(HabitViewModel::class.java)

        habitId = arguments?.getLong("habitId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.details_habit, container, false)
        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        hoursInput = view.findViewById(R.id.hours_layout)
        minutesInput = view.findViewById(R.id.minutes_layout)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHabit()
        initSpinnerAdapters()

        // TODO add edit button to enable editing, default should be uneditable
    }

    private fun getHabit(){
        viewModel.getById(habitId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { habit -> inflateViews(habit) }

    }

    private fun inflateViews(it: Habit){
        Log.i("NAME", "onViewCreated: ${it.name}")
        name.setText(it.name)
        description.setText(it.description)
        var timeMap = getTimeStrings(it.sessionLength)
        (hoursInput.editText as AutoCompleteTextView).setText(timeMap["Hours"])
        (minutesInput.editText as AutoCompleteTextView).setText(timeMap["Minutes"])
    }

    override fun initOnClickListeners() {
        TODO("Not yet implemented")
    }

    override fun initViewModelObservables() {
        TODO("Not yet implemented")
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
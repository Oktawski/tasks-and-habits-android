package com.example.tah.ui.habit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.models.Habit
import com.example.tah.utilities.ViewHabitTime
import com.example.tah.utilities.ViewInits
import com.example.tah.viewModels.HabitViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var startFab: FloatingActionButton
    private lateinit var deleteEditLayout: LinearLayout
    private lateinit var cancelSaveLayout: LinearLayout
    private lateinit var deleteButton: MaterialButton
    private lateinit var editButton: MaterialButton
    private lateinit var cancelButton: MaterialButton
    private lateinit var saveButton: MaterialButton

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
        startFab = view.findViewById(R.id.fab_start)
        deleteEditLayout = view.findViewById(R.id.delete_edit_layout)
        cancelSaveLayout = view.findViewById(R.id.cancel_save_layout)
        deleteButton = view.findViewById(R.id.delete_button)
        editButton = view.findViewById(R.id.edit_button)
        cancelButton = view.findViewById(R.id.cancel_button)
        saveButton = view.findViewById(R.id.save_button)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHabit()
        initSpinnerAdapters()
        initOnClickListeners()

        // TODO add edit button to enable editing, default should be uneditable
    }

    override fun onResume() {
        super.onResume()
        deleteEditLayout.visibility = View.VISIBLE
        cancelSaveLayout.visibility = View.GONE
    }

    private fun getHabit(){
        viewModel.getById(habitId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { habit -> inflateViews(habit) }

    }

    private fun inflateViews(it: Habit){
        name.setText(it.name)
        description.setText(it.description)
        val timeMap = getTimeStrings(it.sessionLength)
        (hoursInput.editText as AutoCompleteTextView).setText(timeMap["Hours"])
        (minutesInput.editText as AutoCompleteTextView).setText(timeMap["Minutes"])
    }

    override fun initOnClickListeners() {
        editButton.setOnClickListener { setEditableView() }
        cancelButton.setOnClickListener { setNotEditableView() }

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

    private fun setEditableView(){
        this.deleteEditLayout.visibility = View.GONE
        this.cancelSaveLayout.visibility = View.VISIBLE
    }

    private fun setNotEditableView(){
        this.deleteEditLayout.visibility= View.VISIBLE
        this.cancelSaveLayout.visibility = View.GONE
    }
}
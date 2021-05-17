package com.example.tah.ui.habit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.tah.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class HabitAddFragment : Fragment() {

    private lateinit var name: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var hourSpinner: Spinner
    private lateinit var minuteSpinner: Spinner
    private lateinit var fabAdd: FloatingActionButton

    private val hours = Array(10){it}
    private val minutes = Array(60){it}

    private var hour: Int = 0
    private var minute: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.add_habit_fragment, container, false)

        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        hourSpinner = view.findViewById(R.id.hour_spinner)
        minuteSpinner = view.findViewById(R.id.minute_spinner)
        fabAdd = view.findViewById(R.id.fab_add)

        initSpinnerAdapters()
        initOnClickListeners()

        return view
    }

    private fun initOnClickListeners(){
        hourSpinner.onItemSelectedListener = createOnItemSelectedListener(hours)
        minuteSpinner.onItemSelectedListener = createOnItemSelectedListener(minutes)

        fabAdd.setOnClickListener {
            val text = "Hour: $hour \n Minute: $minute"
            Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initSpinnerAdapters(){
        createSpinnerAdapter(hourSpinner, hours)
        createSpinnerAdapter(minuteSpinner, minutes)
    }

    private fun createSpinnerAdapter(spinner: Spinner, array: Array<Int>){
        ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            array
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun createOnItemSelectedListener(array: Array<Int>): AdapterView.OnItemSelectedListener {
        return object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(array.size > 24) minute = array[position]
                else hour = array[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireActivity(), "Nothing selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
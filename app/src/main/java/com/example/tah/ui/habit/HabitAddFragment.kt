package com.example.tah.ui.habit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.tah.R
import com.google.android.material.textfield.TextInputEditText

class HabitAddFragment : Fragment() {

    private lateinit var name: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var hourSpinner: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.add_habit_fragment, container, false)

        name = view.findViewById(R.id.name)
        description = view.findViewById(R.id.description)
        hourSpinner = view.findViewById(R.id.hour_spinner)

        return view
    }

}
package com.example.tah.ui.main

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tah.R
import com.example.tah.ViewInits
import com.example.tah.models.TaskViewModel
import com.example.tah.ui.habit.HabitsFragment
import com.example.tah.ui.task.TaskAddFragment
import com.example.tah.ui.task.TasksFragment
import com.example.tah.utilities.State

class AddItemActivity: AppCompatActivity(), ViewInits{

    lateinit var taskViewModel: TaskViewModel
    lateinit var backArrow: ImageButton

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        backArrow = findViewById(R.id.back_arrow)

        val fragmentId: Int = intent.getIntExtra("fragmentId", R.layout.fragment_habits)
        var fragment: Fragment = TasksFragment()

        when(fragmentId){
            R.string.tab_tasks -> fragment = TaskAddFragment()
            R.string.tab_habits -> fragment = HabitsFragment() //TODO implement AddHabitFragment
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.add_fragment_container, fragment)
                .commit()

        initOnClickListeners()
        initViewModelObservables()
    }

    override fun initOnClickListeners() {
        backArrow.setOnClickListener{
            finish()
        }
    }

    override fun initViewModelObservables() {
        TODO("Not yet implemented")
    }
}
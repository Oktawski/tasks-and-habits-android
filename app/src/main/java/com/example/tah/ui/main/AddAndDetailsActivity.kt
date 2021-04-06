package com.example.tah.ui.main

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tah.R
import com.example.tah.ViewInits
import com.example.tah.models.Task
import com.example.tah.ui.habit.HabitsFragment
import com.example.tah.ui.task.TaskAddFragment
import com.example.tah.ui.task.TaskDetailsFragment
import com.example.tah.ui.task.TasksFragment

class AddAndDetailsActivity: AppCompatActivity(), ViewInits{

    lateinit var backArrow: ImageButton

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        backArrow = findViewById(R.id.back_arrow)

        val fragmentId: Int = intent.getIntExtra("fragmentId", R.layout.fragment_habits)

        var fragment = when(fragmentId){
            Task.getAddView() -> TaskAddFragment()
            Task.getDetailsView() -> TaskDetailsFragment()
            //Habit.getAddView() -> HabitAddFragment() //TODO implement HabitAddFragment
            //Habit.getDetailsView() -> HabitDetailsFragment()  // TODO implement HabitDetailsFragment
            else -> PlaceholderFragment()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.add_fragment_container, fragment)
                .commit()

        initOnClickListeners()
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
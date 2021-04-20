package com.example.tah.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.tah.R
import com.example.tah.ViewInits
import com.example.tah.models.Task
import com.example.tah.ui.task.TaskAddFragment
import com.example.tah.ui.task.TaskDetailsFragment

class AddAndDetailsActivity: AppCompatActivity(), ViewInits{

    lateinit var backArrow: ImageButton

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        backArrow = findViewById(R.id.back_arrow)

        val fragment = when(intent.getIntExtra("fragmentId", R.layout.fragment_habits)){
            Task.getAddView() -> TaskAddFragment()
            Task.getDetailsView() -> {
                val name = intent.getStringExtra("name")
                val description = intent.getStringExtra("description")
                Log.i("TAG", "onCreate: $name $description")
                TaskDetailsFragment.newInstance(name, description)
            }
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
package com.example.tah.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tah.R
import com.example.tah.databinding.ActivityAddAndDetailsBinding
import com.example.tah.models.Habit
import com.example.tah.models.Task
import com.example.tah.ui.habit.HabitAddFragment
import com.example.tah.ui.habit.HabitDetailsFragment
import com.example.tah.ui.task.TaskAddFragment
import com.example.tah.ui.task.TaskDetailsFragment
import com.example.tah.utilities.ViewInitializable

class AddAndDetailsActivity :
    AppCompatActivity(),
    ViewInitializable {

    private var _binding: ActivityAddAndDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_and_details)
        _binding = ActivityAddAndDetailsBinding.inflate(layoutInflater)


        val fragment = when(intent.getIntExtra("fragmentId", R.layout.fragment_habits)){
            Task.getAddView() -> TaskAddFragment()
            Task.getDetailsView() -> {
                val name = intent.getStringExtra("name")
                val description = intent.getStringExtra("description")
                Log.i("TAG", "onCreate: $name $description")
                TaskDetailsFragment.newInstance(name, description)
            }
            Habit.getAddView() -> HabitAddFragment()
            Habit.getDetailsView() ->
                HabitDetailsFragment.newInstance(intent.getLongExtra("habitId", -1))
            else -> HabitAddFragment()
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.add_fragment_container, fragment)
                .commit()

        initOnClickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initOnClickListeners() {
        binding.backArrow.setOnClickListener{
            finish()
        }
    }

    override fun initViewModelObservables() {
        TODO("Not yet implemented")
    }
}
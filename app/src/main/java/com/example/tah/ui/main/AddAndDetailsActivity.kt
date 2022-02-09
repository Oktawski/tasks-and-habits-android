package com.example.tah.ui.main

import android.os.Bundle
import androidx.annotation.LayoutRes
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
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity used to store fragments to add new items or fragments that show details of
 * a specific item
 */

@AndroidEntryPoint
class AddAndDetailsActivity :
    AppCompatActivity(R.layout.activity_add_and_details),
    ViewInitializable
{
    private var _binding: ActivityAddAndDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        _binding = ActivityAddAndDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = when(intent.getIntExtra("fragmentId", R.layout.fragment_habits)){
            Task.getAddView() -> TaskAddFragment()
            Task.getDetailsView() ->
                TaskDetailsFragment.newInstance(intent.getLongExtra("taskId", -1))
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

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount == 0) finish()
        else supportFragmentManager.popBackStack()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun initOnClickListeners() {
        binding.backArrow.setOnClickListener { onBackPressed() }
    }

    override fun initViewModelObservables() {
        TODO("Not yet implemented")
    }

    internal fun setDeleteIconVisibility(@LayoutRes visibility: Int) {
        binding.deleteIcon.visibility = visibility
    }

    internal fun setTitle(title: String){
        binding.title.text = title
    }
}

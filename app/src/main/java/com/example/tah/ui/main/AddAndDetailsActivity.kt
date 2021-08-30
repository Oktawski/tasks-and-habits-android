package com.example.tah.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.example.tah.R
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
    ViewInitializable {

    private lateinit var backArrow: ImageView
    private lateinit var deleteButton: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        backArrow = findViewById(R.id.back_arrow)
        deleteButton = findViewById(R.id.delete_icon)
        title = findViewById(R.id.title)

        val fragment = when(intent.getIntExtra("fragmentId", R.layout.fragment_habits)){
            Task.getAddView() -> TaskAddFragment()
            Task.getDetailsView() ->
                TaskDetailsFragment.newInstance(intent.getIntExtra("taskId", -1))
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

    override fun initOnClickListeners() {
        backArrow.setOnClickListener { onBackPressed() }
    }

    override fun initViewModelObservables() {
        TODO("Not yet implemented")
    }

    internal fun setDeleteIconVisibility(@LayoutRes visibility: Int) {
        deleteButton.visibility = visibility
    }

    internal fun setTitle(title: String){
        this.title.text = title
    }

}
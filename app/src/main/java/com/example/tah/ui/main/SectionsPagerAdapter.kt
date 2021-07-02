package com.example.tah.ui.main

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tah.R
import com.example.tah.ui.habit.HabitsFragment
import com.example.tah.ui.task.TasksFragment
import com.example.tah.ui.todo.TodosFragment

class SectionsPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    @StringRes
    private val tabTitles = listOf(R.string.tab_tasks, R.string.tab_todos, R.string.tab_habits)

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TasksFragment()
            1 -> TodosFragment()
            2 -> HabitsFragment()
            else -> TasksFragment()
        }
    }

    fun getTabTitleId(position: Int) = tabTitles[position]
}
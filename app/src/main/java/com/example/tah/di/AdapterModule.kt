package com.example.tah.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.tah.ui.habit.HabitRecyclerViewAdapter
import com.example.tah.ui.main.MainActivity
import com.example.tah.ui.task.TaskRecyclerViewAdapter
import com.example.tah.ui.todo.TodoRecyclerViewAdapter
import com.example.tah.viewModels.TaskViewModel
import com.example.tah.viewModels.TodoViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object AdapterModule {

    @Provides
    @FragmentScoped
    fun provideTaskAdapter(
        @ActivityContext context: Context,
    ): TaskRecyclerViewAdapter {
        val viewModel: TaskViewModel = ViewModelProvider(context as MainActivity).get(TaskViewModel::class.java)
        return TaskRecyclerViewAdapter(context, viewModel, mutableListOf(), mutableListOf())
    }

    @Provides
    @FragmentScoped
    fun provideTodoAdapter(
        @ActivityContext context: Context
    ): TodoRecyclerViewAdapter {
        return TodoRecyclerViewAdapter(
            context,
            mutableListOf(),
            ViewModelProvider(context as MainActivity).get(TodoViewModel::class.java)
        )
    }

    @Provides
    @FragmentScoped
    fun provideHabitAdapter(
        @ActivityContext context: Context,
    ): HabitRecyclerViewAdapter {
        return HabitRecyclerViewAdapter(context, mutableListOf())
    }

}

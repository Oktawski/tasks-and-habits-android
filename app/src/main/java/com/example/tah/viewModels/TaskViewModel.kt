package com.example.tah.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.task.TaskRepository
import com.example.tah.models.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel<Task>()
{
    private val checkBoxVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    init {
        itemsLD = repository.getAll()
        state = repository.state
    }

    fun setCheckBoxVisibility(visibility: Int){
        checkBoxVisibility.value = visibility
    }

    fun getCheckBoxVisibility(): LiveData<Int>{
        return checkBoxVisibility
    }

    override fun add(t: Task) = repository.add(t)

    fun getById(id: Int?): Single<Task> {
        return repository.getById(id)
    }

    override fun delete(t: Task) {
        return repository.delete(t)
    }

    override fun deleteAll() {
        repository.deleteAll()
    }

    override fun deleteSelected() {
        repository.deleteSelected()
    }

    override fun update(t: Task) {
        repository.update(t)
    }

}
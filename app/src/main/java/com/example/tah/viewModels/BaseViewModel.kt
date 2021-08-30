package com.example.tah.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tah.utilities.State

abstract class BaseViewModel<T>
    : ViewModel()
{
    var itemsLD: LiveData<List<T>>? = null
    lateinit var state: MutableLiveData<State>
    var checkedItemsLD: MutableLiveData<List<Int>> = MutableLiveData(emptyList())

    abstract suspend fun add(t: T): Long
    abstract fun delete(t: T)
    abstract fun deleteAll()
    abstract fun deleteSelected()
    abstract fun update(t: T)

    fun addToCheckedItems(idList: List<Int>){
        checkedItemsLD.value = idList
    }

    fun clearCheckedItems(){
        checkedItemsLD.value = emptyList()
    }

}

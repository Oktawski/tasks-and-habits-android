package com.example.tah.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tah.utilities.State

abstract class BaseViewModel<T>
    : ViewModel()
{
    val state: MutableLiveData<State> = MutableLiveData()
    var itemsLD: LiveData<List<T>>? = null
    var checkedItemsLD: MutableLiveData<List<Long>> = MutableLiveData(emptyList())

    abstract fun add(t: T)
    abstract fun delete(t: T)
    abstract fun deleteAll()
    abstract fun deleteSelected()
    abstract fun update(t: T)

    fun addToCheckedItems(idList: List<Long>){
        checkedItemsLD.value = idList
    }

    fun clearCheckedItems(){
        checkedItemsLD.value = emptyList()
    }

}

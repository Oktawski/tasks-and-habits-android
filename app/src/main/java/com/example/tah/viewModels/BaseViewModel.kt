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
    lateinit var itemsLD: LiveData<List<T>>
    lateinit var state: MutableLiveData<State>
    var checkedItemsLD: MutableLiveData<List<Int>> = MutableLiveData(emptyList())

    abstract fun add(t: T)
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
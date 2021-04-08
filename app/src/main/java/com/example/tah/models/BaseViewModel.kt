package com.example.tah.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.utilities.State
import java.util.*

abstract class BaseViewModel<T>(application: Application)
    : AndroidViewModel(application) {

    lateinit var itemsLD: LiveData<List<T>>
    var checkedItemsLD: MutableLiveData<List<Int>> = MutableLiveData()
    lateinit var state: MutableLiveData<State>


    fun addToCheckedItems(idList: List<Int>){
        checkedItemsLD.value = idList
    }

    abstract fun add(t: T)
    abstract fun delete(t: T)
    abstract fun deleteAll()
    abstract fun deleteSelected()
}
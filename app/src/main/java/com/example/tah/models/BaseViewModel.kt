package com.example.tah.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel<T>(application: Application)
    : AndroidViewModel(application) {

    lateinit var itemsLD: LiveData<List<T>>
    var checkedItemsLD: MutableLiveData<List<Integer>> = MutableLiveData()


    fun addToCheckedItems(idList: List<Integer>){
        checkedItemsLD.value = idList
    }

    abstract fun add(t: T)
    abstract fun delete(t: T)
    abstract fun deleteAll()


}
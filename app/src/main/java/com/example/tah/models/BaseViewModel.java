package com.example.tah.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public abstract class BaseViewModel<T> extends AndroidViewModel {

    protected LiveData<List<T>> itemsLD;
    protected final MutableLiveData<List<Integer>> checkedItemsLD = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<T>> getItemsLD() {
        return itemsLD;
    }

    public LiveData<List<Integer>> getCheckedItemsLD() {
        return checkedItemsLD;
    }

    public void addToCheckedItems(List<Integer> idList) {
        checkedItemsLD.setValue(idList);
    }

    public abstract void add(T t);
    public abstract void delete(T t);
    public abstract void deleteAll();
}


package com.example.tah.dao.habit

import androidx.lifecycle.MutableLiveData
import com.example.tah.models.Habit
import com.example.tah.utilities.PropertiesTrimmer
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitRepository @Inject constructor(
    private val dao: HabitDao
) : PropertiesTrimmer {

    val state = MutableLiveData<State>()

    private val disposable = CompositeDisposable()

    fun getAll() = dao.getAll()

    suspend fun getById(id: Long?): Habit {
        return dao.getById(id)
    }

    suspend fun add(t: Habit): Long {
        state.value = State.loading()

        var habitId = -1L

        trimLeadingAndTrailingWhitespaces(t)
        CoroutineScope(Dispatchers.Main).launch {
            habitId = dao.insert(t)
            state.value = State.added("Habit added")
        }

        return habitId
/*
        disposable.add(dao.insert(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.added("Habit added")},
                {state.value = State.error("Error")}))*/
    }

    suspend fun delete(t: Habit) {
        state.value = State.loading()
        dao.delete(t)

        /*disposable.add(dao.delete(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.removed("Habit removed")},
                {state.value = State.error("Error")}))*/
    }

    fun deleteAll() {
        disposable.add(dao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state.value = State.removed("All habits removed") },
                { state.value = State.error("Error") }))
    }

    fun deleteSelected() {

    }

    fun update(t: Habit) {
        trimLeadingAndTrailingWhitespaces(t)

        disposable.add(dao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state.value = State.updated("Habit updated") },
                { state.value = State.error("Error") }))
    }

}
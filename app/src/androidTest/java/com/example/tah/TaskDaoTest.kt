package com.example.tah

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tah.dao.task.TaskDao
import com.example.tah.dao.task.TaskDatabase
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.Executors


@RunWith(AndroidJUnit4::class)
class TaskDatabaseTest : LiveDataUtil {

    private lateinit var taskDao: TaskDao
    private lateinit var taskDatabase: TaskDatabase
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun createDb() {
        taskDatabase = Room.inMemoryDatabaseBuilder(
            context, TaskDatabase::class.java
        )
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        taskDao = taskDatabase.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        taskDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeTaskAndGetAllItems() {
        val task = Task("name", "desc", TaskType.BASIC, false)
        var tasks: LiveData<List<Task>>

        runBlocking {
            taskDao.insert(task)
            tasks = taskDao.getAll()
        }

        tasks.getOrAwait()
        assert(tasks.value?.size == 1)
    }

    @Test
    @Throws(Exception::class)
    fun writeTaskAndGetItemById() {
        val task = Task("name", "desc", TaskType.BASIC, false)
        var taskInserted: Task?

        runBlocking {
            taskDao.insert(task)
            taskInserted = taskDao.getTaskById(1)
        }

        assertEquals(task.name, taskInserted?.name)
        (task.name == taskInserted?.name)
    }

    @Test
    @Throws(Exception::class)
    fun insertMultipleDeleteAll() {
        val task = Task("name", "desc", TaskType.BASIC, false)
        var tasks: LiveData<List<Task>>?

        runBlocking {
            taskDao.insert(task)
            taskDao.insert(task)
            taskDao.insert(task)
            taskDao.insert(task)
            taskDao.insert(task)
            taskDao.insert(task)
            taskDao.deleteAll()
            tasks = taskDao.getAll()
        }

        assertEquals(tasks.getOrAwait()?.size, 0)
    }

    @Test
    fun insertMultipleFilterByTaskType() {
        val taskShopping = Task("shopping", "", TaskType.SHOPPING, false)
        val taskOther = Task("other", "desc", TaskType.BASIC, false)
        var shoppingTasks: LiveData<List<Task>>
        var basicTasks: LiveData<List<Task>>

        runBlocking {
            taskDao.insert(taskShopping)
            taskDao.insert(taskOther)
            taskDao.insert(taskShopping)
            taskDao.insert(taskOther)
            taskDao.insert(taskShopping)
            taskDao.insert(taskOther)
            taskDao.insert(taskShopping)
            taskDao.insert(taskOther)

            shoppingTasks = taskDao.getFilteredTasks(TaskType.SHOPPING)
            basicTasks = taskDao.getFilteredTasks(TaskType.BASIC)
        }

        val shoppingMatchedStream = shoppingTasks.getOrAwait()
            ?.stream()
            ?.allMatch { e -> e.type == TaskType.SHOPPING }

        val basicMatchedStream = basicTasks.getOrAwait()
            ?.stream()
            ?.allMatch { e -> e.type == TaskType.BASIC }

        val basicMatchedStreamFalse = basicTasks.getOrAwait()
            ?.stream()
            ?.anyMatch { e -> e.type in listOf(TaskType.HOME, TaskType.SHOPPING, TaskType.LEARNING) }

        assertEquals(shoppingMatchedStream, true)
        assertEquals(basicMatchedStream, true)
        assertEquals(basicMatchedStreamFalse, false)
    }

}

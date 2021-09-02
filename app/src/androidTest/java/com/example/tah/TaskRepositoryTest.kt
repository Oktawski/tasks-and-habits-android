package com.example.tah

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tah.dao.task.TaskDao
import com.example.tah.dao.task.TaskDatabase
import com.example.tah.dao.task.TaskRepository
import com.example.tah.dao.todo.TodoDao
import com.example.tah.dao.todo.TodoDatabase
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class TaskRepositoryTest : LiveDataUtil {

    private lateinit var taskDao: TaskDao
    private lateinit var todoDao: TodoDao
    private lateinit var taskDatabase: TaskDatabase
    private lateinit var todoDatabase: TodoDatabase
    private lateinit var taskRepository: TaskRepository
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun createDb() {
        taskDatabase = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        todoDatabase = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        taskDao = taskDatabase.taskDao()
        todoDao = todoDatabase.todoDao()

        taskRepository = TaskRepository(taskDao, todoDao)

        val basicTask = Task("basic", "", TaskType.BASIC, false)
        val shoppingTask = Task("shopping", "", TaskType.SHOPPING, false)

        runBlocking {
            withContext(Dispatchers.Main) {
                taskRepository.add(basicTask)
                taskRepository.add(shoppingTask)
                taskRepository.add(basicTask)
                taskRepository.add(shoppingTask)
                taskRepository.add(basicTask)
                taskRepository.add(shoppingTask)
                taskRepository.add(basicTask)
                taskRepository.add(shoppingTask)
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        taskDatabase.close()
        todoDatabase.close()
    }

    @Test
    fun insertMultipleGetAll() {
        var tasksLiveData: LiveData<List<Task>>?

        runBlocking {
            withContext(Dispatchers.Main) {
                tasksLiveData = taskRepository.getAll()
            }
        }
        assertEquals(tasksLiveData.getOrAwait()?.size, 8)
    }

    @Test
    fun insertMultipleGetFiltered() {
        var filteredTasks: LiveData<List<Task>>

        runBlocking {
            filteredTasks = taskRepository.getFiltered(TaskType.SHOPPING)
        }

        assertEquals(
            filteredTasks.getOrAwait()?.stream()?.allMatch { e -> e.type == TaskType.SHOPPING },
            true
        )
    }

}

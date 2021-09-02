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
import com.example.tah.viewModels.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executors

@RunWith(JUnit4::class)
class TaskViewModelTest : LiveDataUtil {

    private lateinit var taskDao: TaskDao
    private lateinit var todoDao: TodoDao
    private lateinit var taskDatabase: TaskDatabase
    private lateinit var todoDatabase: TodoDatabase
    private lateinit var taskRepository: TaskRepository
    private lateinit var taskViewModel: TaskViewModel
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
        taskViewModel = TaskViewModel(taskRepository)

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

    @Test
    @Throws(Exception::class)
    fun addMultipleGetAll2() {
        val task = Task("task", "desc", TaskType.BASIC, false)
        val tasksLD: LiveData<List<Task>>

        runBlocking {
            withContext(Dispatchers.Main) {
                taskViewModel.add(task)
                taskViewModel.add(task)
                taskViewModel.add(task)
                taskViewModel.add(task)
                taskViewModel.add(task)
                taskViewModel.add(task)
                taskViewModel.add(task)
                taskViewModel.add(task)
            }
        }

        taskViewModel.getFiltered(TaskType.SHOPPING)

        assertEquals(
            taskViewModel.itemsLD?.getOrAwait()?.stream()?.allMatch { e -> e.type == TaskType.SHOPPING },
            true
        )

        taskViewModel.getAll()

        assertEquals(
            taskViewModel.itemsLD?.getOrAwait()?.stream()?.allMatch { e -> e.type == TaskType.SHOPPING },
           false
        )

    }



}
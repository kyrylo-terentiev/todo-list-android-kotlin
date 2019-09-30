package com.onpu.todolist_app.data

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TodoRepository(application: Application) {

    private val todoDao: TodoDao
    private val allTodos: LiveData<List<TodoRecord>>

    init {
        val db = TodoDatabase.getInstance(application.applicationContext)
        todoDao = db!!.todoDao()
        allTodos = todoDao.getAllTodos()
    }

    fun saveTodo(todo: TodoRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            todoDao.saveTodo(todo)
        }
    }

    fun updateTodo(todo: TodoRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            todoDao.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: TodoRecord) = runBlocking {
        this.launch(Dispatchers.IO) {
            todoDao.deleteTodo(todo)
        }
    }

    fun getAllTodos(): LiveData<List<TodoRecord>> {
        return allTodos
    }
}
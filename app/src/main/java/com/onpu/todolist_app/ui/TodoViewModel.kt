package com.onpu.todolist_app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.onpu.todolist_app.data.TodoRecord
import com.onpu.todolist_app.data.TodoRepository

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: TodoRepository = TodoRepository(application)
    private val allTodos: LiveData<List<TodoRecord>> = repo.getAllTodos()

    fun saveTodo(todo: TodoRecord) {
        repo.saveTodo(todo)
    }

    fun updateTodo(todo: TodoRecord) {
        repo.updateTodo(todo)
    }

    fun deleteTodo(todo: TodoRecord) {
        repo.deleteTodo(todo)
    }

    fun getAllTodos(): LiveData<List<TodoRecord>> {
        return allTodos
    }
}
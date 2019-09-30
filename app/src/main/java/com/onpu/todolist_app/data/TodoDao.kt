package com.onpu.todolist_app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoRecord ORDER BY id ASC")
    fun getAllTodos(): LiveData<List<TodoRecord>>

    @Query("SELECT * FROM todoRecord WHERE id=:tid")
    fun getTodo(tid: Int): TodoRecord

    @Insert
    suspend fun saveTodo(todo: TodoRecord)

    @Update
    suspend fun updateTodo(todo: TodoRecord)

    @Delete
    suspend fun deleteTodo(todo: TodoRecord)
}
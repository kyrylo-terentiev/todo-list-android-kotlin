package com.terentiev.notes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE isArchived=0 ORDER BY id ASC")
    fun getActiveNotes(): LiveData<List<NoteRecord>>

    @Query("SELECT * FROM notes WHERE isArchived=1 ORDER BY id ASC")
    fun getArchivedNotes(): LiveData<List<NoteRecord>>

    @Query("SELECT * FROM notes WHERE id=:tid")
    fun getNote(tid: Int): NoteRecord

    @Insert
    suspend fun saveNote(note: NoteRecord)

    @Update
    suspend fun updateNote(note: NoteRecord)

    @Delete
    suspend fun deleteNote(note: NoteRecord)

}
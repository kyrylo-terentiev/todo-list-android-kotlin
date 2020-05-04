package com.terentiev.notes.data

import android.app.Application
import android.util.Log.d
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NoteRepository(application: Application) {
    private val TAG = "NoteRepository"
    private val noteDao: NoteDao
    private val activeNotes: LiveData<List<NoteRecord>>
    private val archivedNotes: LiveData<List<NoteRecord>>

    init {
        val db = NoteDatabase.getInstance(application.applicationContext)
        noteDao = db!!.todoDao()
        activeNotes = noteDao.getActiveNotes()
        archivedNotes = noteDao.getArchivedNotes()
    }

    fun saveNote(note: NoteRecord) = runBlocking {
        d(TAG, ":saveNote()")
        this.launch(Dispatchers.IO) {
            noteDao.saveNote(note)
        }
    }

    fun updateNote(note: NoteRecord) = runBlocking {
        d(TAG, ":updateNote()")
        this.launch(Dispatchers.IO) {
            noteDao.updateNote(note)
        }
    }

    fun deleteNote(note: NoteRecord) = runBlocking {
        d(TAG, ":deleteNote()")
        this.launch(Dispatchers.IO) {
            noteDao.deleteNote(note)
        }
    }

    fun getActiveNotes(): LiveData<List<NoteRecord>> {
        d(TAG, ":getActiveNotes()")
        return activeNotes
    }

    fun getArchivedNotes(): LiveData<List<NoteRecord>> {
        d(TAG, ":getArchivedNotes()")
        return archivedNotes
    }

}
package com.onpu.todolist_app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.onpu.todolist_app.data.NoteRecord
import com.onpu.todolist_app.data.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: NoteRepository = NoteRepository(application)

    fun saveNote(note: NoteRecord) {
        repo.saveNote(note)
    }

    fun updateNote(note: NoteRecord) {
        repo.updateNote(note)
    }

    fun deleteNote(note: NoteRecord) {
        repo.deleteNote(note)
    }

    fun getActiveNotes(): LiveData<List<NoteRecord>> {
        return repo.getActiveNotes()
    }

    fun getArchivedNotes(): LiveData<List<NoteRecord>> {
        return repo.getArchivedNotes()
    }

    fun archiveNote(note: NoteRecord) {
        note.isArchived = 1
        repo.updateNote(note)
    }

    fun unarchiveNote(note: NoteRecord) {
        note.isArchived = 0
        repo.updateNote(note)
    }
}
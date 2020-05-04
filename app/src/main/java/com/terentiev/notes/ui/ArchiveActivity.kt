package com.terentiev.notes.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.terentiev.notes.R
import com.terentiev.notes.data.NoteRecord
import com.terentiev.notes.utils.ItemSwipeCallback

import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.content_archive.*

class ArchiveActivity : AppCompatActivity(), NoteListAdapter.TodoEvents {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteListAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rv_archive.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteListAdapter(this)
        rv_archive.adapter = noteAdapter
        ItemTouchHelper(
            ItemSwipeCallback(
                noteAdapter,
                applicationContext,
                true
            )
        ).attachToRecyclerView(rv_archive)

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.getArchivedNotes().observe(this, Observer {
            noteAdapter.setAllTodos(it)
        })
    }

    override fun onItemDeleted(note: NoteRecord, position: Int) {
        noteViewModel.deleteNote(note)
        noteAdapter.notifyItemRemoved(position)
        val snackbar = Snackbar.make(container_archive, R.string.deleted, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.undo) { undoDelete(note, position) }
        snackbar.show()
    }

    private fun undoDelete(note: NoteRecord, position: Int) {
        noteViewModel.saveNote(note)
        noteAdapter.notifyItemInserted(position)
    }

    private fun undoUnarchive(note: NoteRecord, position: Int) {
        noteViewModel.archiveNote(note)
        noteAdapter.notifyItemInserted(position)
    }

    override fun onViewClicked(note: NoteRecord) {
        // do nothing
    }

    override fun onItemUnarchived(note: NoteRecord, position: Int) {
        noteViewModel.unarchiveNote(note)
        noteAdapter.notifyItemRemoved(position)
        val snackbar = Snackbar.make(container_archive, R.string.unarchived, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.undo) { undoUnarchive(note, position) }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_archive, menu)
        searchView = menu?.findItem(R.id.search_archived_item)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                noteAdapter.filter.filter(query)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                noteAdapter.filter.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search_archived_item -> true
            R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        resetSearchView()
        super.onBackPressed()
    }

    private fun resetSearchView() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
    }
}

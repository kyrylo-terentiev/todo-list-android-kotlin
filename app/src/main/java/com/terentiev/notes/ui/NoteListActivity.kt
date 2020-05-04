package com.terentiev.notes.ui

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.terentiev.notes.R
import com.terentiev.notes.data.NoteRecord
import com.terentiev.notes.utils.Constants
import com.terentiev.notes.utils.ItemSwipeCallback
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*


class NoteListActivity : AppCompatActivity(), NoteListAdapter.TodoEvents {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var searchView: SearchView
    private lateinit var noteAdapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        rv_todo_list.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteListAdapter(this)
        rv_todo_list.adapter = noteAdapter
        ItemTouchHelper(
            ItemSwipeCallback(
                noteAdapter,
                applicationContext,
                false
            )
        ).attachToRecyclerView(rv_todo_list)

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel.getActiveNotes().observe(this, Observer {
            noteAdapter.setAllTodos(it)
        })

        fab_new_todo.setOnClickListener {
            resetSearchView()
            val intent = Intent(this@NoteListActivity, CreateNoteActivity::class.java)
            startActivityForResult(intent, Constants.INTENT_CREATE_NOTE)
        }
    }

    override fun onItemDeleted(note: NoteRecord, position: Int) {
        noteViewModel.archiveNote(note)
        val snackbar = Snackbar.make(container, R.string.archived, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.undo) { undoArchive(note, position) }
        snackbar.show()
    }

    private fun undoArchive(note: NoteRecord, position: Int) {
        noteViewModel.unarchiveNote(note)
        noteAdapter.notifyItemInserted(position)
    }

    override fun onViewClicked(note: NoteRecord) {
        resetSearchView()
        val intent = Intent(this@NoteListActivity, CreateNoteActivity::class.java)
        intent.putExtra(Constants.INTENT_OBJECT, note)
        startActivityForResult(intent, Constants.INTENT_UPDATE_NOTE)
    }

    override fun onItemUnarchived(note: NoteRecord, position: Int) {
        onItemDeleted(note, position)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val todo = data?.getParcelableExtra<NoteRecord>(Constants.INTENT_OBJECT)!!
            when (requestCode) {
                Constants.INTENT_CREATE_NOTE -> {
                    noteViewModel.saveNote(todo)
                }
                Constants.INTENT_UPDATE_NOTE -> {
                    noteViewModel.updateNote(todo)
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        searchView = menu?.findItem(R.id.search_active_item)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
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
            R.id.search_active_item -> true
            R.id.open_archive_item -> {
                val i = Intent(this, ArchiveActivity::class.java)
                this.startActivity(i)
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

package com.terentiev.notes.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.terentiev.notes.R
import com.terentiev.notes.data.NoteRecord
import com.terentiev.notes.utils.Constants
import kotlinx.android.synthetic.main.activity_create_note.*
import kotlinx.android.synthetic.main.content_create_note.*

class CreateNoteActivity : AppCompatActivity() {

    private var noteRecord: NoteRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val intent = intent
        if (intent != null && intent.hasExtra(Constants.INTENT_OBJECT)) {
            val note: NoteRecord = intent.getParcelableExtra(Constants.INTENT_OBJECT)
            this.noteRecord = note
            prePopulateData(note)
            et_todo_title.setSelection(et_todo_title.text.length)
        }
        toolbar.title = if (noteRecord != null) getString(R.string.editNote) else getString(R.string.createNote)

    }

    private fun prePopulateData(note: NoteRecord) {
        et_todo_title.setText(note.title)
        et_todo_content.setText(note.content)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_note -> {
                saveTodo()
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true;
    }

    private fun saveTodo() {
        if (validateFields()) {
            val id = if (noteRecord != null) noteRecord?.id else null
            val todo = NoteRecord(
                id = id, title = et_todo_title.text.toString(),
                content = et_todo_content.text.toString(),
                isArchived = 0
            )
            val intent = Intent()
            intent.putExtra(Constants.INTENT_OBJECT, todo)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun validateFields(): Boolean {
        if (et_todo_title.text.isEmpty()) {
            til_todo_title.error = getString(R.string.pleaseEnterTitle)
            et_todo_title.requestFocus()
            return false
        }
        if (et_todo_content.text.isEmpty()) {
            til_todo_content.error = getString(R.string.pleaseEnterContent)
            et_todo_content.requestFocus()
            return false
        }
        return true
    }

}

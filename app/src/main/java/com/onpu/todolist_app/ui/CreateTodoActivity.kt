package com.onpu.todolist_app.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.onpu.todolist_app.R
import com.onpu.todolist_app.data.TodoRecord
import com.onpu.todolist_app.utils.Constants
import kotlinx.android.synthetic.main.activity_create_todo.toolbar
import kotlinx.android.synthetic.main.content_create_todo.*

class CreateTodoActivity : AppCompatActivity() {

    var todoRecord: TodoRecord? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val intent = intent
        if (intent != null && intent.hasExtra(Constants.INTENT_OBJECT)) {
            val todo: TodoRecord = intent.getParcelableExtra(Constants.INTENT_OBJECT)
            this.todoRecord = todo
            prePopulateData(todo)
        }

        title =
            if (todoRecord != null) getString(R.string.viewOrEditTodo) else getString(R.string.createTodo)
    }

    private fun prePopulateData(todo: TodoRecord) {
        et_todo_title.setText(todo.title)
        et_todo_content.setText(todo.content)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_todo -> {
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
            val id = if (todoRecord != null) todoRecord?.id else null
            val todo = TodoRecord(
                id = id, title = et_todo_title.text.toString(),
                content = et_todo_content.text.toString()
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

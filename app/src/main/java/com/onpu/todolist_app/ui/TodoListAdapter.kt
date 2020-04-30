package com.onpu.todolist_app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.onpu.todolist_app.R
import com.onpu.todolist_app.data.TodoRecord
import kotlinx.android.synthetic.main.todo_item.view.*

class TodoListAdapter(todoEvents: TodoEvents) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>(),
    Filterable {

    private var todos: List<TodoRecord> = arrayListOf()
    private var filteredTodoList: List<TodoRecord> = arrayListOf()
    private val listener: TodoEvents = todoEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filteredTodoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredTodoList[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(todo: TodoRecord, listener: TodoEvents) {
            itemView.card_title_tv.text = todo.title
            itemView.card_content_tv.text = todo.content
//            itemView.iv_item_delete.setOnClickListener {
//                listener.onDeleteClicked(todo)
//            }
            itemView.setOnClickListener {
                listener.onViewClicked(todo)
            }
        }
    }

    fun setAllTodos(todos: List<TodoRecord>) {
        this.todos = todos
        this.filteredTodoList = todos
        notifyDataSetChanged()
    }

    interface TodoEvents {
        fun onDeleteClicked(todo: TodoRecord, position: Int)
        fun onViewClicked(todo: TodoRecord)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredTodoList = if (charString.isEmpty()) {
                    todos
                } else {
                    val filteredList = arrayListOf<TodoRecord>()
                    for (row in todos) {
                        if (row.title!!.toLowerCase().contains(charString.toLowerCase())
                            || row.content!!.contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredTodoList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredTodoList = p1?.values as List<TodoRecord>
                notifyDataSetChanged()
            }

        }
    }

    fun deleteItem(position: Int) {
        listener.onDeleteClicked(todos[position], position)
    }


}

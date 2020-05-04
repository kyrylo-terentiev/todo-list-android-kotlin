package com.terentiev.notes.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.terentiev.notes.R
import com.terentiev.notes.data.NoteRecord
import kotlinx.android.synthetic.main.note_item.view.*

class NoteListAdapter(todoEvents: TodoEvents) : RecyclerView.Adapter<NoteListAdapter.ViewHolder>(),
    Filterable {

    private var notes: List<NoteRecord> = arrayListOf()
    private var filteredNoteList: List<NoteRecord> = arrayListOf()
    private val listener: TodoEvents = todoEvents

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filteredNoteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredNoteList[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: NoteRecord, listener: TodoEvents) {
            itemView.card_title_tv.text = note.title
            itemView.card_content_tv.text = note.content
            itemView.setOnClickListener {
                listener.onViewClicked(note)
            }
        }
    }

    fun setAllTodos(notes: List<NoteRecord>) {
        this.notes = notes

        this.filteredNoteList = notes
        notifyDataSetChanged()
    }

    interface TodoEvents {
        fun onItemDeleted(note: NoteRecord, position: Int)
        fun onViewClicked(note: NoteRecord)
        fun onItemUnarchived(note: NoteRecord, position: Int)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredNoteList = if (charString.isEmpty()) {
                    notes
                } else {
                    val filteredList = arrayListOf<NoteRecord>()
                    for (row in notes) {
                        if (row.title!!.toLowerCase().contains(charString.toLowerCase())
                            || row.content!!.toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredNoteList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredNoteList = p1?.values as List<NoteRecord>
                notifyDataSetChanged()
            }

        }
    }

    fun deleteItem(position: Int) {
        listener.onItemDeleted(notes[position], position)
    }

    fun restoreItem(position: Int) {
        listener.onItemUnarchived(notes[position], position)
    }
}

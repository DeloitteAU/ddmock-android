package com.dd.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dd.R
import kotlinx.android.synthetic.main.view_entry.view.*

class MockAdapter(private val callback: Callback) : RecyclerView.Adapter<MockAdapter.EntryViewHolder>() {

    var items = ArrayList<String>()
        set(value) {
            items.clear()
            items.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder =
            EntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_entry, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.text.text = items[position]
        holder.itemView.setOnClickListener { callback.onEntryClicked(items[position]) }
    }

    class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = itemView.entryText
    }

    interface Callback {
        fun onEntryClicked(key: String)
    }
}
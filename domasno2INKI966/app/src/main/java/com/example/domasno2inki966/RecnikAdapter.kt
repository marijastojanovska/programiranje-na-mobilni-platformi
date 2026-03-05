package com.example.domasno1inki966

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView
import com.example.domasno2inki966.R

class RecnikAdapter(private var items: List<RecnikItem>,
                    private val onEditClick: (RecnikItem) -> Unit) :
    RecyclerView.Adapter<RecnikAdapter.RecnikViewHolder>() {

    class RecnikViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recnikButton: MaterialButton = itemView.findViewById(R.id.recnikButton)
        val editButton: MaterialButton = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecnikViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recnik, parent, false)
        return RecnikViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecnikViewHolder, position: Int) {
        val item = items[position]

        holder.recnikButton.text = "${item.angliski}:\n${item.makedonski}"
        holder.editButton.text = "Измени"

        holder.recnikButton.setOnClickListener {

        }

        holder.editButton.setOnClickListener {
            onEditClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<RecnikItem>) {
        items = newList.toMutableList()
        notifyDataSetChanged()
    }
}
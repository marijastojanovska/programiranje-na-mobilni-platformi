package com.example.domasno1inki966

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView

class TagAdapter(private val tagList: List<TagItem>,
                 private val onEditClick: (TagItem) -> Unit) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagButton: MaterialButton = itemView.findViewById(R.id.tagButton)
        val editButton: MaterialButton = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tag, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val item = tagList[position]

        holder.tagButton.text = item.tag
        holder.editButton.text = "Edit"

        holder.tagButton.setOnClickListener {

        }

        holder.editButton.setOnClickListener {
            onEditClick(item)
        }
    }

    override fun getItemCount(): Int = tagList.size
}
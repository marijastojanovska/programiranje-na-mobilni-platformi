package com.example.domasno1inki966

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var searchInput: TextInputEditText
    private lateinit var tagInput: TextInputEditText
    private lateinit var saveBtn: MaterialButton
    private lateinit var clearBtn: MaterialButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TagAdapter
    private val tagList = mutableListOf<TagItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<View>(R.id.rootLayout)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                top = systemBars.top,
                bottom = systemBars.bottom
            )
            insets
        }

        // Initialize views
        searchInput = findViewById(R.id.searchInput)
        tagInput = findViewById(R.id.tagInput)
        saveBtn = findViewById(R.id.saveBtn)
        clearBtn = findViewById(R.id.clearBtn)
        recyclerView = findViewById(R.id.recyclerView)

        // Setup RecyclerView
        adapter = TagAdapter(tagList) {
            tagInput.setText(it.tag)
            searchInput.setText(it.value)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        saveBtn.setOnClickListener {

            val tagText = tagInput.text.toString()
            val valueText = searchInput.text.toString()

            if (tagText.isNotEmpty() && valueText.isNotEmpty()) {

                // Проверка дали веќе постои tag
                val existingItem = tagList.find { it.tag == tagText }

                if (existingItem != null) {
                    // UPDATE
                    existingItem.value = valueText
                } else {
                    // ADD NEW
                    tagList.add(TagItem(tagText, valueText))
                }

                adapter.notifyDataSetChanged()

                tagInput.text?.clear()
                searchInput.text?.clear()
            }
        }

        // Clear button click
        clearBtn.setOnClickListener {
            tagList.clear()
            adapter.notifyDataSetChanged()

            tagInput.text?.clear()
            searchInput.text?.clear()
        }
    }
}
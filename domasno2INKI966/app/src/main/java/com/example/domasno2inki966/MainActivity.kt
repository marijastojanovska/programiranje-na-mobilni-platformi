package com.example.domasno2inki966

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domasno1inki966.RecnikAdapter
import com.example.domasno1inki966.RecnikItem
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var angliskiInput: TextInputEditText
    private lateinit var makedonskiInput: TextInputEditText
    private lateinit var searchInput: TextInputEditText
    private lateinit var saveBtn: MaterialButton
    private lateinit var clearBtn: MaterialButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecnikAdapter
    private val recnikList = mutableListOf<RecnikItem>()

    private val fileName = "recnik.txt"

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

        initViews()
        loadFromFile()
    }

    private fun initViews() {
        // Initialize views
        angliskiInput = findViewById(R.id.angliskiInput)
        makedonskiInput = findViewById(R.id.makedonskiInput)
        searchInput = findViewById(R.id.searchInput)
        saveBtn = findViewById(R.id.saveBtn)
        clearBtn = findViewById(R.id.clearBtn)
        recyclerView = findViewById(R.id.recyclerView)

        // Setup RecyclerView
        adapter = RecnikAdapter(recnikList) {
            makedonskiInput.setText(it.makedonski)
            angliskiInput.setText(it.angliski)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        saveBtn.setOnClickListener {

            val makedonskiZbor = makedonskiInput.text.toString()
            val angliskiZbor = angliskiInput.text.toString()

            if (makedonskiZbor.isNotEmpty() && angliskiZbor.isNotEmpty()) {

                val existingItem = recnikList.find { it.makedonski == makedonskiZbor }

                if (existingItem != null) {
                    existingItem.angliski = angliskiZbor
                } else {
                    recnikList.add(RecnikItem(angliskiZbor, makedonskiZbor))
                }

                searchInput.text?.clear()

                adapter.updateList(recnikList)
                adapter.notifyDataSetChanged()
                saveToFile()


                makedonskiInput.text?.clear()
                angliskiInput.text?.clear()
            }
        }

        // Clear button click
        clearBtn.setOnClickListener {
            recnikList.clear()
            adapter.notifyDataSetChanged()

            angliskiInput.text?.clear()
            makedonskiInput.text?.clear()

            saveToFile()
        }

        searchInput.addTextChangedListener {

            val query = it.toString().lowercase()

            if (query.isEmpty()) {

                adapter.updateList(recnikList)

            } else {

                val filteredList = recnikList.filter {

                    it.angliski.lowercase().contains(query) ||
                            it.makedonski.lowercase().contains(query)

                }

                adapter.updateList(filteredList)
            }
        }
    }

    private fun loadFromFile() {

        try {

            val fileInput = openFileInput(fileName)
            val lines = fileInput.bufferedReader().readLines()

            if (lines.isEmpty()) {
                seedWords()
                return
            }

            for (line in lines) {

                val parts = line.split(",")

                if (parts.size == 2) {
                    val makedonski = parts[0]
                    val angliski = parts[1]

                    recnikList.add(RecnikItem(angliski, makedonski))
                }
            }

            adapter.notifyDataSetChanged()

        } catch (e: Exception) {
            seedWords()
        }
    }

    private fun saveToFile() {
        val text = StringBuilder()

        for (item in recnikList) {
            text.append(item.makedonski)
            text.append(",")
            text.append(item.angliski)
            text.append("\n")
        }

        openFileOutput(fileName, MODE_PRIVATE).use {
            it.write(text.toString().toByteArray())
        }
    }

    private fun seedWords() {

        recnikList.add(RecnikItem("hello", "здраво"))
        recnikList.add(RecnikItem("dog", "куче"))
        recnikList.add(RecnikItem("cat", "мачка"))
        recnikList.add(RecnikItem("water", "вода"))
        recnikList.add(RecnikItem("food", "храна"))

        adapter.notifyDataSetChanged()

        saveToFile()
    }
}
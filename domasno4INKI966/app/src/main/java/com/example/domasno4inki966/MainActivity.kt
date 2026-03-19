package com.example.domasno4inki966

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domasno4inki966.ui.theme.Domasno4INKI966Theme
import com.example.domasno4inki966.ui.theme.RecnikItem

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Domasno4INKI966Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RecnikApp()
                }
            }
        }
    }

    @Composable
    fun RecnikApp() {

        val context = LocalContext.current

        var angliski by remember { mutableStateOf("") }
        var makedonski by remember { mutableStateOf("") }
        var search by remember { mutableStateOf("") }

        var recnikList by remember { mutableStateOf(mutableListOf<RecnikItem>()) }

        // Load data
        LaunchedEffect(Unit) {
            recnikList = loadFromFile(context).toMutableList()
        }

        val filteredList = recnikList.filter {
            it.angliski.contains(search, true) ||
                    it.makedonski.contains(search, true)
        }

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Македонско/Англиски речник",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = angliski,
                onValueChange = { angliski = it },
                label = { Text("Англиски збор") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                OutlinedTextField(
                    value = makedonski,
                    onValueChange = { makedonski = it },
                    label = { Text("Македонски збор") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {

                    if (angliski.isNotEmpty() && makedonski.isNotEmpty()) {

                        val existing = recnikList.find {
                            it.makedonski == makedonski
                        }

                        if (existing != null) {
                            existing.angliski = angliski
                        } else {
                            recnikList.add(RecnikItem(angliski, makedonski))
                        }

                        recnikList = recnikList.toMutableList()

                        saveToFile(context, recnikList)

                        angliski = ""
                        makedonski = ""
                        search = ""
                    }

                }) {
                    Text("Зачувај")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Пребарај збор") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Зборови", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(filteredList) { item ->

                    RecnikRow(item) {
                        angliski = item.angliski
                        makedonski = item.makedonski
                    }
                }
            }

            Button(
                onClick = {
                    recnikList.clear()
                    recnikList = recnikList.toMutableList()
                    saveToFile(context, recnikList)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Бриши речник")
            }
        }
    }

    @Composable
    fun RecnikRow(item: RecnikItem, onClick: () -> Unit) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { onClick() }
        ) {

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.makedonski)
                Text(" - ")
                Text(item.angliski)
            }
        }
    }

    fun saveToFile(context: Context, list: List<RecnikItem>) {

        val text = StringBuilder()

        for (item in list) {
            text.append(item.makedonski)
            text.append(",")
            text.append(item.angliski)
            text.append("\n")
        }

        context.openFileOutput("recnik.txt", Context.MODE_PRIVATE).use {
            it.write(text.toString().toByteArray())
        }
    }

    fun loadFromFile(context: Context): List<RecnikItem> {

        return try {

            val lines = context.openFileInput("recnik.txt")
                .bufferedReader()
                .readLines()

            if (lines.isEmpty()) {
                return listOf()
            } else {
                lines.mapNotNull {

                    val parts = it.split(",")

                    if (parts.size == 2) {
                        RecnikItem(parts[1], parts[0])
                    } else null
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            listOf()
        }
    }
}

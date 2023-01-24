package com.example.notes

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var title : EditText
    private lateinit var text : EditText
    private lateinit var create : Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        title = findViewById(R.id.title)
        text = findViewById(R.id.text)
        create = findViewById(R.id.create_btn)

        val dao=NotesDatabase.getDatabase(this).notesdao()
        val repository = NotesRepository(dao)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]
        var check: Int = intent . getIntExtra("id", 0)
        val noteData = intent.getStringExtra("note")
        val note = Gson().fromJson(noteData, Notes::class.java)

        if (check != 100000){
            title.setText(note.title)
            text.setText(note.text)
        }

        create.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.US)
            val dateTime = simpleDateFormat.format(calendar.time)

            if (check == 100000){
                mainViewModel.insertNote(Notes(0, title.text.toString(), text.text.toString(),
                    "Last Modified on: \n$dateTime"
                ))
                Toast.makeText(this, "Note created successfully :)", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                check = note.id
                mainViewModel.updateNote(Notes(check, title.text.toString(), text.text.toString(),
                    "Last Modified on: \n$dateTime"
                    ))
                Toast.makeText(this, "Note updated successfully :)", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
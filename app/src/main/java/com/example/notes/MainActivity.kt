package com.example.notes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), NoteItemListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NotesDatabase
    private lateinit var mainViewModel: MainViewModel
    private lateinit var note: Notes
    private lateinit var adapter: RVAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        database = NotesDatabase.getDatabase(this)
        val dao = database.notesdao()
        val repository = NotesRepository(dao)

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = RVAdapter(this, this)
        binding.recyclerView.adapter = adapter

        binding.addNoteBtn.setOnClickListener {
            startActivity(Intent(this, AddNote::class.java).putExtra("id", 100000))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }
        })

        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.getNotes().observe(this) {
            it?.let {
                adapter.updateList(it)
            }
        }
    }

    override fun onItemClicked(notes: Notes) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        val json = Gson().toJson(notes)
        intent.putExtra("note", json)
        startActivity(intent)
    }

    override fun onLongItemClicked(notes: Notes, cardView: CardView) {
        note = notes
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popUp = PopupMenu(this, cardView)
        popUp.setOnMenuItemClickListener(this)
        popUp.inflate(R.menu.pop_up_menu)
        popUp.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_note) {
            mainViewModel.deleteNote(note)
            return true
        }
        return false
    }
}
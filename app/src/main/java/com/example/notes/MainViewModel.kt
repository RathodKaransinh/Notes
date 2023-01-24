package com.example.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val notesRepository: NotesRepository) : ViewModel() {

    fun getNotes() : LiveData<List<Notes>>{
        return notesRepository.getNotes()
    }

    fun insertNote(note: Notes){
        viewModelScope.launch(Dispatchers.IO){
            notesRepository.insertNote(note)
        }
    }

    fun deleteNote(note: Notes){
        viewModelScope.launch(Dispatchers.IO){
            notesRepository.deleteNote(note)
        }
    }

    fun updateNote(note: Notes){
        viewModelScope.launch(Dispatchers.IO){
            notesRepository.updateNote(note)
        }
    }
}
package com.example.notes

import androidx.lifecycle.LiveData

class NotesRepository(private val notesDao: NotesDao) {
    fun getNotes(): LiveData<List<Notes>> {
        return notesDao.getNotes()
    }

    suspend fun insertNote(note : Notes){
        notesDao.insertNote(note)
    }

    suspend fun deleteNote(note: Notes){
        notesDao.deleteNote(note)
    }

    suspend fun updateNote(note: Notes){
        notesDao.update(note.id, note.title, note.text)
    }
}
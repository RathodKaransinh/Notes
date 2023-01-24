package com.example.notes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Query("SELECT * from notes")
    fun getNotes() : LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

    @Query("UPDATE notes Set title = :title, text = :note WHERE id = :id")
    suspend fun update(id : Int?, title : String?, note : String?)
}

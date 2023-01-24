package com.example.notes

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class RVAdapter(
    private val context: Context,
    private val listener: NoteItemListener
): RecyclerView.Adapter<ViewHolder>(){

    private val notesList = ArrayList<Notes>()
    private val fullList = ArrayList<Notes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_rv, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currNote=notesList[position]
        holder.title.text=currNote.title
        holder.title.isSelected=true

        holder.text.text=currNote.text
        holder.date.text=currNote.date
        holder.date.isSelected=true

        holder.cv.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.cv.setOnClickListener {
            listener.onItemClicked(notesList[holder.adapterPosition])
        }

        holder.cv.setOnLongClickListener{
            listener.onLongItemClicked(notesList[holder.adapterPosition], holder.cv)
            true
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Notes>){
        fullList.clear()
        fullList.addAll(newList)

        notesList.clear()
        notesList.addAll(fullList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(search: String){
        notesList.clear()

        for (item in fullList){
            if (item.title.lowercase().contains(search.lowercase()) || item.text.lowercase().contains(search.lowercase())){
                notesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    private fun randomColor() : Int{
        val list=ArrayList<Int>()
        list.add(R.color.clr1)
        list.add(R.color.clr2)
        list.add(R.color.clr3)
        list.add(R.color.clr4)
        list.add(R.color.clr5)
        list.add(R.color.clr6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }
}

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.title_txt)
    val text: TextView = itemView.findViewById(R.id.text_txt)
    val date: TextView = itemView.findViewById(R.id.date_txt)
    val cv: CardView = itemView.findViewById(R.id.card_layout)
}

interface NoteItemListener{
    fun onItemClicked(notes: Notes)
    fun onLongItemClicked(notes: Notes, cardView: CardView)
}
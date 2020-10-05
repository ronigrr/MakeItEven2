package com.example.makeiteven2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.data_models.NameAndScoreInfo
import kotlinx.android.synthetic.main.score_board_cell.view.*

class ScoreBoardCellAdapter(private var mScoresList:List<NameAndScoreInfo>,private val context: Context) :
    RecyclerView.Adapter<ScoreBoardCellAdapter.ViewHolder>()
{
    class ViewHolder(private val view : View,private val context: Context) : RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bind(list: NameAndScoreInfo, position: Int)
        {
            view.tvName.text = list.playerName
            view.tvScore.text = list.playerScore
            view.tvPlace.text = "${position+1}."
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mScoresList[position].let { list -> holder.bind(list,position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.score_board_cell, parent, false)
        return ViewHolder(view, context)
    }

    override fun getItemCount(): Int {
        return mScoresList.size
    }
    fun updateScoreBoardList(newList : List<NameAndScoreInfo>){
        mScoresList = newList
        notifyDataSetChanged()
    }
}
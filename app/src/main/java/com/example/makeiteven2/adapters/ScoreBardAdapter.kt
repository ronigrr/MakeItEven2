package com.example.makeiteven2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.data_models.Level
import com.example.makeiteven2.extras.Animations
import com.example.makeiteven2.fragments.ScoreBoardCell
import com.example.makeiteven2.intefaces.ILevelsAdapter
import com.example.makeiteven2.intefaces.IScoreBoardAdapter
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.level_cell.view.*

class ScoreBardAdapter(private var mList: List<ScoreBoardCell> = ArrayList(),
                       private val context: Context) :
    RecyclerView.Adapter<ScoreBardAdapter.ViewHolder>() {

    var mCallBack: IScoreBoardAdapter

    init {
        if (context is IScoreBoardAdapter) {
            mCallBack = context
        } else {
            throw ClassCastException(context.toString() + "must implement IScoreBoardAdapter")
        }
    }


    class ViewHolder(private val view: View,
                     private val context: Context) : RecyclerView.ViewHolder(view) {

        fun bind(scoreBoardCell: ScoreBoardCell, callback: IScoreBoardAdapter) {

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.level_cell, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mList[position].let { list -> holder.bind(list, mCallBack) }
    }

    override fun getItemCount(): Int {
        return mList.size
    }


}

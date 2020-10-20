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
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.level_cell.view.*

class LevelsAdapter(private var mLevelsList: List<Level> = ArrayList(), private val context: Context, private var mCurrentStage: Int) :
    RecyclerView.Adapter<LevelsAdapter.ViewHolder>() {

    var mCallBack: ILevelsAdapter

    init {
        if (context is ILevelsAdapter) {
            mCallBack = context
        } else {
            throw ClassCastException(context.toString() + "must implement ILevelAdapter")
        }
    }

    interface ILevelsAdapter {
        fun onLevelsAdapterItemClicked(levelNumber: Int)
    }

    class ViewHolder(private val view: View, private val context: Context) : RecyclerView.ViewHolder(view) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(level: Level, currentStage: Int, callback: ILevelsAdapter) {
            view.tvStageNumber.text = level.levelNum.toString()
            view.btnLevel.setOnTouchListener(Animations.getTouchAnimation(context))

            when {
                level.levelNum == 0 -> {
                    view.layoutStageNumber.visibility = View.VISIBLE
                    view.ivLock.visibility = View.INVISIBLE
                    view.tvStageNumber.setText(R.string.tutorial)
                }
                level.levelNum <= currentStage -> {
                    view.layoutStageNumber.visibility = View.VISIBLE
                    view.ivLock.visibility = View.INVISIBLE
                }
                level.levelNum > currentStage -> {
                    view.ivLock.visibility = View.VISIBLE
                    view.layoutStageNumber.visibility = View.INVISIBLE
                }
            }
            if (level.levelNum == currentStage){
                Animations.setFadeInOutAnimationToLevel(view)
            }

            view.btnLevel.setOnClickListener {
                if (view.ivLock.visibility == View.VISIBLE) {
                    Toasty.custom(context, "You did not reach this stage yet", null, Toasty.LENGTH_SHORT, false).show()
                } else {
                    callback.onLevelsAdapterItemClicked(level.levelNum)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.level_cell, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mLevelsList[position].let { list -> holder.bind(list, mCurrentStage, mCallBack) }
    }

    override fun getItemCount(): Int {
        return mLevelsList.size
    }

}
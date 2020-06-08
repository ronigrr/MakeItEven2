package com.example.makeiteven2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.data_models.Level
import com.example.makeiteven2.extras.OnTouchAnimation
import com.example.makeiteven2.fragments.FragmentLevelsScreen
import kotlinx.android.synthetic.main.level_cell.view.*

class LevelsAdapter(private var mLevelsList : List<Level> = ArrayList(), private val context: Context,private var mCurrentStage : Int)
    : RecyclerView.Adapter<LevelsAdapter.ViewHolder>(){

    var mCallBack : ILevelsAdapter

    init {
        if (context is ILevelsAdapter) {
            mCallBack = context
        }
        else{
            throw ClassCastException(context.toString() + "must implement ILevelAdapter")
        }
    }

    interface ILevelsAdapter{
        fun levelsAdapterItemClicked(levelNumber : Int)
    }

    class ViewHolder(private val view : View,private val context: Context) : RecyclerView.ViewHolder(view){

        @SuppressLint("ClickableViewAccessibility")
        fun bind(level : Level, currentStage: Int,callback : ILevelsAdapter) {
            val onTouchAnimation = OnTouchAnimation(context)
            view.tvStageNumber.text = level.levelNum.toString()
            view.btnLevel.setOnTouchListener(onTouchAnimation.btnTouchAnimation)

            when  {
                level.levelNum == 0 -> {
                    view.layoutStageNumber.visibility = View.VISIBLE
                    view.ivLock.visibility = View.INVISIBLE
                    view.tvStageNumber.setText(R.string.tutorial)
                }
                level.levelNum <= currentStage-> {
                    view.layoutStageNumber.visibility = View.VISIBLE
                    view.ivLock.visibility = View.INVISIBLE
                }
                level.levelNum > currentStage -> {
                    view.ivLock.visibility = View.VISIBLE
                    view.layoutStageNumber.visibility = View.INVISIBLE
                }
            }

            view.btnLevel.setOnClickListener {
                callback.levelsAdapterItemClicked(level.levelNum)
                //TODO: Implement onClick listener with callback to start the game level
            }

        }
    }

    //TODO: need to think if setLevels and setCurrentStage is relevant
    fun setLevels(levelList : List<Level>){
        this.mLevelsList = levelList
        notifyDataSetChanged()
    }

    fun setCurrentStage(currentStage : Int){
        mCurrentStage = currentStage
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.level_cell,parent,false)
        return ViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mLevelsList[position].let { list ->holder.bind(list,mCurrentStage,mCallBack) }
    }

    override fun getItemCount(): Int {
        return mLevelsList.size
    }

}
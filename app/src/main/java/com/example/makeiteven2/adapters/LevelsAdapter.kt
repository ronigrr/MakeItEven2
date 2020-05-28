package com.example.makeiteven2.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.data_models.Level
import com.example.makeiteven2.extras.OnTouchAnimation
import kotlinx.android.synthetic.main.level_cell.view.*

class LevelsAdapter(private var mLevelsList : List<Level> = ArrayList(), private val context: Context,private var mCurrentStage : Int)
    : RecyclerView.Adapter<LevelsAdapter.ViewHolder>(){

    class ViewHolder(private val view : View,private val context: Context) : RecyclerView.ViewHolder(view){
        private val onTouchAnimation = OnTouchAnimation(context)
        private lateinit var levelBtn : Button
        private lateinit var lockStage : View
        private lateinit var stageNumberLayout : View
        private lateinit var stageNumberTv : TextView

        @SuppressLint("ClickableViewAccessibility")
        fun bind(level : Level, currentStage: Int) {
            levelBtn = view.btnLevel
            lockStage= view.ivLock
            stageNumberLayout = view.stage_number_layout
            stageNumberTv = view.tvStageNumber

            stageNumberTv.text = level.levelNum.toString()
            levelBtn.setOnTouchListener(onTouchAnimation.btnTouchAnimation)

            when  {
                level.levelNum == 0 -> {
                    stageNumberLayout.visibility = View.VISIBLE
                    lockStage.visibility = View.INVISIBLE
                    stageNumberTv.setText(R.string.tutorial)
                }
                level.levelNum <= currentStage-> {
                    stageNumberLayout.visibility = View.VISIBLE
                    lockStage.visibility = View.INVISIBLE
                }
                level.levelNum > currentStage -> {
                    lockStage.visibility = View.VISIBLE
                    stageNumberLayout.visibility = View.INVISIBLE
                }
            }

            levelBtn.setOnClickListener {
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
        mLevelsList[position].let { list ->holder.bind(list,mCurrentStage) }
    }

    override fun getItemCount(): Int {
        return mLevelsList.size
    }

}
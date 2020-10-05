package com.example.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.adapters.LevelsAdapter
import com.example.makeiteven2.adapters.ScoreBardAdapter
import com.example.makeiteven2.data_models.Level
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.intefaces.IFragmentLevelsScreenListener
import com.example.makeiteven2.intefaces.IFragmentScoreBoardListener
import kotlinx.android.synthetic.main.fragment_levels.view.*
import kotlinx.android.synthetic.main.fragment_score_board.view.*

class FragmentScoreBoard : Fragment(){
    private lateinit var mCallBack: IFragmentScoreBoardListener
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: ScoreBardAdapter
    var mItemsList: ArrayList<ScoreBoardCell> = ArrayList()

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    fun updateList(list: ArrayList<ScoreBoardCell>){
        mItemsList = list
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = layoutInflater.inflate(R.layout.fragment_score_board, container)
        mRecyclerView = rootView.recyclerLevels
        mAdapter = ScoreBardAdapter(mItemsList, rootView.context)
        rootView.ibBack.setOnClickListener {
            mCallBack.onScoreFragmentBackPressed()
        }
        initLevels()
        initRecyclerView()

        return rootView
    }

    private fun initRecyclerView() {
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mAdapter
    }

    private fun initLevels() {
        if (mItemsList.isEmpty()){
        for (i in 1..100){
            var num = "1564"
            mItemsList.add(ScoreBoardCell("#.","samib",num.toInt().inc().toString()))
        }
    }}
}
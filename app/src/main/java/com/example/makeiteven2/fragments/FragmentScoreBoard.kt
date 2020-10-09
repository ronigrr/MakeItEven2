package com.example.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.adapters.ScoreBoardCellAdapter
import com.example.makeiteven2.data_models.NameAndScoreInfo
import com.example.makeiteven2.intefaces.IFragmentScoreBoardScreenListener
import com.example.makeiteven2.room.FireBaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_score_board.view.*


class FragmentScoreBoard : Fragment() {

    private lateinit var mCallBack: IFragmentScoreBoardScreenListener
    private lateinit var mScoreBoardRecyclerView : RecyclerView
    private lateinit var mScoreBoardCellAdapter: ScoreBoardCellAdapter
    var mScoreItemsList : ArrayList<NameAndScoreInfo> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_score_board, container, false)
        mScoreBoardRecyclerView = rootView.scoreBoardRecycler

        mScoreBoardCellAdapter = ScoreBoardCellAdapter(mScoreItemsList, rootView.context)
        rootView.btnBackScoreboard.setOnClickListener {
            mCallBack.onScoreBoardFragmentBackPressed()
        }
        initRecyclerView()
        getDataFromWebAndUpdateRecyclerViewAdapter()
        mCallBack.hide3dotToolBar()
        return rootView
    }

    private fun getDataFromWebAndUpdateRecyclerViewAdapter() {
        FireBaseHelper.getScoreBoardListFromDataBase(context!!,mScoreBoardCellAdapter)
    }

    private fun initRecyclerView() {
        mScoreBoardRecyclerView.setHasFixedSize(true)
        mScoreBoardRecyclerView.layoutManager = LinearLayoutManager(context)
        mScoreBoardRecyclerView.adapter = mScoreBoardCellAdapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentScoreBoardScreenListener) {
            mCallBack = context
        } else {
            throw RuntimeException(context.toString() + "The activity must implement IFragmentScoreBoardScreenListener interface")
        }
    }
}
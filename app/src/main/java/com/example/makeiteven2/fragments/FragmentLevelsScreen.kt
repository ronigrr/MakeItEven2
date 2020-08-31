package com.example.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.adapters.LevelsAdapter
import com.example.makeiteven2.data_models.Level
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.intefaces.IFragmentLevelsScreenListener
import kotlinx.android.synthetic.main.fragment_levels.view.*

class FragmentLevelsScreen : Fragment() {

    private lateinit var mCallBack: IFragmentLevelsScreenListener
    private lateinit var mLevelsRecyclerView: RecyclerView
    private lateinit var mLevelsAdapter: LevelsAdapter
    var mLevelItemsList: ArrayList<Level> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_levels, container, false)
        mLevelsRecyclerView = rootView.recyclerLevels
        mLevelsAdapter = LevelsAdapter(mLevelItemsList, rootView.context, Constants.User.currentLevel)
        rootView.ibBack.setOnClickListener {
            mCallBack.onLevelsFragmentBackPressed()
        }
        initLevelsAdapter()
        initLevels()
        initRecyclerView()

        return rootView
    }

    private fun initLevelsAdapter() {
    }

    private fun initLevels() {
        for (i in 1..100)
            mLevelItemsList.add(Level(i))
    }

    private fun initRecyclerView() {
        mLevelsRecyclerView.setHasFixedSize(true)
        mLevelsRecyclerView.layoutManager = GridLayoutManager(context, 4)
        mLevelsRecyclerView.adapter = mLevelsAdapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentLevelsScreenListener) {
            mCallBack = context
        } else {
            throw RuntimeException(context.toString() + "The activity must implement IFragmentLevelsScreenListener interface")
        }
    }
}
package com.example.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.makeiteven2.R
import com.example.makeiteven2.adapters.LevelsAdapter
import com.example.makeiteven2.data_models.Level
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.intefaces.IFragmentLevelsScreenListener
import com.example.makeiteven2.managers.AnimationsManager
import com.example.makeiteven2.managers.AudioManager
import com.example.makeiteven2.room.DatabaseHelper
import kotlinx.android.synthetic.main.fragment_levels.view.*


class FragmentLevelsScreen : Fragment() {

    private lateinit var mCallBack: IFragmentLevelsScreenListener
    private lateinit var mLevelsRecyclerView: RecyclerView
    private lateinit var mLevelsAdapter: LevelsAdapter
    private var mLevelItemsList: ArrayList<Level> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_levels, container, false)
        initTransition()
        mLevelsRecyclerView = rootView.recyclerLevels
        mLevelsAdapter = LevelsAdapter(mLevelItemsList, rootView.context, Constants.User.currentLevel)
        rootView.ibBack.apply {
            startAnimation(AnimationsManager.getInstance(context!!).getScaleInAnimation())
            setOnClickListener {
                mCallBack.onLevelsFragmentBackPressed()
            }
        }
        initLevels()
        initRecyclerView()
        mCallBack.levelsFragmentClose3dotToolBar()
        return rootView
    }

    private fun initTransition() {
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    private fun initLevels() {
        for (i in 1..500) {
            mLevelItemsList.add(Level(i))
        }
    }

    private fun initRecyclerView() {
        mLevelsRecyclerView.setHasFixedSize(true)

        val layout = GridLayoutManager(context, Constants.calculateNoOfColumns(context!!, 90))

        layout.apply {
            isUsingSpansToEstimateScrollbarDimensions = true
            isSmoothScrollbarEnabled = true
        }

        mLevelsRecyclerView.layoutManager = layout
        mLevelsRecyclerView.adapter = mLevelsAdapter
        mLevelsRecyclerView.scrollToPosition(DatabaseHelper.getStageInfoList().lastIndex)
        mLevelsRecyclerView.layoutAnimation = LayoutAnimationController(AnimationsManager.getInstance(context!!).getFastScaleInAnimation())
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentLevelsScreenListener) {
            mCallBack = context
        } else {
            throw RuntimeException(context.toString() + "The activity must implement IFragmentLevelsScreenListener interface")
        }
    }

    override fun onResume() {
        super.onResume()
        AudioManager.getInstance(context!!).playLoopMusicForSpecificFragment(Constants.LEVELS_SCREEN_FRAGMENT_TAG)
    }
}
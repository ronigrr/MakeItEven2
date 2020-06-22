package com.example.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.example.makeiteven2.R

class FragmentStageModeScreen : Fragment() {

    lateinit var mLevelTV: TextView
    lateinit var mHintsLeftTV: TextView
    lateinit var mDesiredNumberTV: TextView
    lateinit var mBackButtonIB: ImageButton
    lateinit var mRetryButtonIB: ImageButton
    lateinit var mHintIB: ImageButton

    lateinit var mGameButton1TB: ToggleButton
    lateinit var mGameButton2TB: ToggleButton
    lateinit var mGameButton3TB: ToggleButton
    lateinit var mGameButton4TB: ToggleButton

    lateinit var mGamePlusTB: ToggleButton
    lateinit var mGameMinusTB: ToggleButton
    lateinit var mGameMulTB: ToggleButton
    lateinit var mGameDivTB: ToggleButton

    lateinit var mGameButtonsList: List<ToggleButton>
    lateinit var mOperatorsList: List<ToggleButton>

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_game,container,false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}
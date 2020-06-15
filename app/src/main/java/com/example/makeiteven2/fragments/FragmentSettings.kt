package com.example.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.Constants
import kotlinx.android.synthetic.main.fragment_setting.view.*

class FragmentSettings : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting,container,false)
        mMainVolumeBar = rootView.seekBarMainSound
        mSoundEffectsBar =rootView.seekBarSoundEffects
        mResetGameBtn=rootView.btnGameReset
        mExitBtn=rootView.btnCloseSettings

        rootView.seekBarMainSound.progress = Constants.User.mainSoundLevel!!
        rootView.seekBarSoundEffects.progress = Constants.User.soundEffectsLevel!!

        mExitBtn.setOnClickListener{
            callBack.onExitFromSettingsFragment()
        }
        //TODO:finish settings fragment after game logic and datastore is working

        return rootView
    }

    private lateinit var callBack : SettingsFragmentCallBack
    private lateinit var mMainVolumeBar: SeekBar
    private lateinit var mSoundEffectsBar :SeekBar
    private lateinit var mResetGameBtn: Button
    private lateinit var mExitBtn: ImageButton

    internal interface SettingsFragmentCallBack {
        fun onSeekBarMainVolume(mainVolume: Int)
        fun onSeekBarSoundEffects(soundEffectsVolume: Int)
        fun onResetGame()
        fun onExitFromSettingsFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingsFragmentCallBack) {
            callBack = context
        } else {
            throw RuntimeException(context.toString() + "The activity must implement SettingsFragmentCallBack interface")
        }
    }
}
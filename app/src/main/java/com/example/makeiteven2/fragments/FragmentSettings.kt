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
import kotlinx.android.synthetic.main.fragment_setting.view.*

class FragmentSettings : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting,container,false)
        mainVolumeBar = rootView.seekBarMainSound
        soundEffectsBar =rootView.seekBarSoundEffects
        resetGameBtn=rootView.btnGameReset
        exitBtn=rootView.btnCloseSettings

        exitBtn.setOnClickListener{
            callBack.onExitFromSettingsFragment()
        }
        //TODO:finish settings fragment after game logic and datastore is working

        return rootView
    }

    private lateinit var callBack : SettingsFragmentCallBack
    private lateinit var mainVolumeBar: SeekBar
    private lateinit var soundEffectsBar :SeekBar
    private lateinit var resetGameBtn: Button
    private lateinit var exitBtn: ImageButton

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
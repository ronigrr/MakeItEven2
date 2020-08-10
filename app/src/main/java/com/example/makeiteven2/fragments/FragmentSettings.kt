package com.example.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.Constants
import kotlinx.android.synthetic.main.fragment_setting.view.*

class FragmentSettings : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)
        mMainVolumeBar = rootView.seekBarMainSound
        mSoundEffectsBar = rootView.seekBarSoundEffects
        mExitBtn = rootView.btnCloseSettings

        rootView.seekBarMainSound.progress = Constants.User.mainSoundLevel
        rootView.seekBarSoundEffects.progress = Constants.User.soundEffectsLevel

        rootView.btnGameReset.setOnClickListener {
            mCallBack.onResetGame()
        }

        mExitBtn.setOnClickListener {
            mCallBack.onExitFromSettingsFragment()
        }

        val seekBarListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int, fromUser: Boolean
            ) {
                if (seekBar.id == R.id.seekBarMainSound) {
                    Constants.User.mainSoundLevel = seekBar.progress
                    mCallBack.onSeekBarMainVolume(progress)
                }
                if (seekBar.id == R.id.seekBarSoundEffects) {
                    Constants.User.soundEffectsLevel = seekBar.progress
                    mCallBack.onSeekBarSoundEffects(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
        mSoundEffectsBar.setOnSeekBarChangeListener(seekBarListener)
        mMainVolumeBar.setOnSeekBarChangeListener(seekBarListener)
        return rootView
    }

    private lateinit var mCallBack: SettingsFragmentCallBack
    private lateinit var mMainVolumeBar: SeekBar
    private lateinit var mSoundEffectsBar: SeekBar
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
            mCallBack = context
        } else {
            throw RuntimeException(context.toString() + "The activity must implement SettingsFragmentCallBack interface")
        }
    }
}
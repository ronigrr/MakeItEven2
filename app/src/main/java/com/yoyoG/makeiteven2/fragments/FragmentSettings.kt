package com.yoyoG.makeiteven2.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.yoyoG.makeiteven2.R
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.firebase.FireBaseHelper
import com.yoyoG.makeiteven2.intefaces.IFragmentSettingsListener
import com.yoyoG.makeiteven2.managers.AnimationsManager
import com.yoyoG.makeiteven2.room.DatabaseHelper.changePlayerNickname
import kotlinx.android.synthetic.main.fragment_setting.view.*

class FragmentSettings : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)
        mMainVolumeBar = rootView.seekBarMainSound
        mSoundEffectsBar = rootView.seekBarSoundEffects
        mExitBtn = rootView.btnCloseSettings

        rootView.IBtnEditNickname.setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        rootView.btnGameReset.setOnTouchListener(AnimationsManager.getInstance(context!!).getTouchAnimation())
        rootView.etNickname.setText(Constants.User.playerName)

        rootView.seekBarMainSound.progress = Constants.User.mainSoundLevel
        rootView.seekBarSoundEffects.progress = Constants.User.soundEffectsLevel

        rootView.btnGameReset.setOnClickListener {
            mListener.onResetGame()
        }

        mExitBtn.setOnClickListener {
            mListener.onExitFromSettingsFragment()
        }

        rootView.IBtnEditNickname.setOnClickListener {
            when (isEditMode) {
                false -> {
                    isEditMode = true
                    (it as ImageButton).setImageResource(R.drawable.ic_baseline_done_24)
                    rootView.etNickname.isEnabled = true
                }
                true -> {
                    isEditMode = false
                    when (rootView.etNickname.text.toString()) {
                        "" -> {
                            rootView.etNickname.setText(Constants.User.playerName)
                        }
                        Constants.User.playerName -> {
                        }
                        else -> {
                            changePlayerNickname(context!!, rootView.etNickname.text.toString())
                            FireBaseHelper.updateScoreBoardUserNickName(rootView.etNickname.text.toString())
                        }

                    }
                    (it as ImageButton).setImageResource(R.drawable.ic_baseline_edit_24)
                    rootView.etNickname.isEnabled = false
                }
            }
        }

        val seekBarListener: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int, fromUser: Boolean
            ) {
                if (seekBar.id == R.id.seekBarMainSound) {
                    Constants.User.mainSoundLevel = seekBar.progress
                    mListener.onSeekBarMainVolume(progress)
                }
                if (seekBar.id == R.id.seekBarSoundEffects) {
                    Constants.User.soundEffectsLevel = seekBar.progress
                    mListener.onSeekBarSoundEffects(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }
        mSoundEffectsBar.setOnSeekBarChangeListener(seekBarListener)
        mMainVolumeBar.setOnSeekBarChangeListener(seekBarListener)

        return rootView
    }

    private lateinit var mListener: IFragmentSettingsListener
    private lateinit var mMainVolumeBar: SeekBar
    private lateinit var mSoundEffectsBar: SeekBar
    private lateinit var mExitBtn: ImageButton
    private var isEditMode = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentSettingsListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + "The activity must implement SettingsFragmentCallBack interface")
        }
    }
}
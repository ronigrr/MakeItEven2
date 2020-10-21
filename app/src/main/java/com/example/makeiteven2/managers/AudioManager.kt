package com.example.makeiteven2.managers

import android.content.Context
import android.media.MediaPlayer
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.Constants

object AudioManager {


    private var mGameMediaPlayer = MediaPlayer()
    private var mEffectsMediaPlayer = MediaPlayer()
    private var mSoundEffectsVolume = 0f
    private var mMainSoundVolume = 0f


    fun setGameVolume(mainVolume: Int) {
        mGameMediaPlayer.setVolume(mainVolume.toFloat() / 100, mainVolume.toFloat() / 100)
    }

    fun setEffectVolume(effectsVolume: Int) {
        mEffectsMediaPlayer.setVolume(
            effectsVolume.toFloat() / 100, effectsVolume.toFloat() / 100
        )
    }

    fun playBtnOn(context: Context) {
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.btn_on_sound)
        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mEffectsMediaPlayer.start()
    }

    fun playBtnOff(context: Context) {
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mGameMediaPlayer = MediaPlayer.create(context, R.raw.btn_off_sound)
        mGameMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mGameMediaPlayer.start()
    }

    fun startWaWaSound(context: Context) {
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.waa_waa_waaaa)
        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mEffectsMediaPlayer.start()
    }

    fun startArcadeSuccessSound(context: Context) {
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.success_arcade)
        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mEffectsMediaPlayer.start()
    }

    fun startTaDaSound(context: Context) {
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.ta_da)
        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mEffectsMediaPlayer.start()
    }

    fun startGameMusic(context: Context) {
        mMainSoundVolume = Constants.User.mainSoundLevel.toFloat()
        mGameMediaPlayer = MediaPlayer.create(context, R.raw.super_duper_by_ian_post)
        mGameMediaPlayer.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mGameMediaPlayer.isLooping = true
        mGameMediaPlayer.start()
    }

    fun stopGameMusic() {
        mGameMediaPlayer.stop()
    }

    fun startWrongAnswerSound(context: Context) {
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.wrong_answer)
        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mEffectsMediaPlayer.start()
    }

}
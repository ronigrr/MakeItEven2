package com.example.makeiteven2.extras

import android.content.Context
import android.media.MediaPlayer
import com.example.makeiteven2.R

class AudioManager {
    companion object {

        private var mInstance: AudioManager? = null
        private var mGameMediaPlayer = MediaPlayer()
        private var mEffectsMediaPlayer = MediaPlayer()
        private lateinit var mContext: Context
        private var mSoundEffectsVolume = 0f
        private var mCurrentMainVolume = 0f

        @Synchronized
        fun getInstance(context: Context): Companion {
            mContext = context
            if (mInstance == null) {
                mInstance = AudioManager()
            }
            return Companion
        }

        fun setGameVolume(mainVolume: Int) {
            mGameMediaPlayer.setVolume(mainVolume.toFloat() / 100, mainVolume.toFloat() / 100)
        }

        fun setEffectVolume(effectsVolume: Int) {
            mEffectsMediaPlayer.setVolume(
                effectsVolume.toFloat() / 100, effectsVolume.toFloat() / 100
            )
        }

        fun playBtnOn() {
            mSoundEffectsVolume = (Constants.User.soundEffectsLevel / 100).toFloat()
            mEffectsMediaPlayer = MediaPlayer.create(mContext, R.raw.btn_on_sound)
            mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
            mEffectsMediaPlayer.start()
        }

        fun playBtnOff() {
            mSoundEffectsVolume = (Constants.User.soundEffectsLevel / 100).toFloat()
            mGameMediaPlayer = MediaPlayer.create(mContext, R.raw.btn_off_sound)
            mGameMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
            mGameMediaPlayer.start()
        }

        fun startWaWaSound() {

            mEffectsMediaPlayer = MediaPlayer.create(mContext, R.raw.waa_waa_waaaa)
            mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
            mEffectsMediaPlayer.start()
        }

        fun startTaDaSound() {
            mSoundEffectsVolume = (Constants.User.soundEffectsLevel / 100).toFloat()
            mEffectsMediaPlayer = MediaPlayer.create(mContext, R.raw.ta_da)
            mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
            mEffectsMediaPlayer.start()
        }

        fun startGameMusic() {
            mCurrentMainVolume = Constants.User.mainSoundLevel.toFloat()
            mGameMediaPlayer = MediaPlayer.create(mContext, R.raw.super_duper_by_ian_post)
            mGameMediaPlayer.setVolume(mCurrentMainVolume / 100, mCurrentMainVolume / 100)
            mGameMediaPlayer.isLooping = true
            mGameMediaPlayer.start()
        }

        fun stopGameMusic() {
            mGameMediaPlayer.stop()
        }

        fun startBuzzerSound(){
            mSoundEffectsVolume = (Constants.User.soundEffectsLevel / 100).toFloat()
            mEffectsMediaPlayer = MediaPlayer.create(mContext, R.raw.buzzer_sound)
            mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
            mEffectsMediaPlayer.start()
        }
    }
}
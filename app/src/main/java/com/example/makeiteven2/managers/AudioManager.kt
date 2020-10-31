package com.example.makeiteven2.managers

import android.content.Context
import android.media.MediaPlayer
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.Constants

object AudioManager {


    private var mGameMediaPlayer = MediaPlayer()
    private var mGameBeginsSound    = MediaPlayer()
    private var mShortGameLoopSound = MediaPlayer()
    private var mLongGameLoopSound  = MediaPlayer()
    private var mEffectsMediaPlayer = MediaPlayer()
    private var mTadaEffectMediaPlayer = MediaPlayer()
    private var mWawaEffectMediaPlayer = MediaPlayer()
    private var mBtnOnEffectMediaPlayer = MediaPlayer()
    private var mBtnOffEffectMediaPlayer = MediaPlayer()
    private var mArcadeWrongAnswerEffectMediaPlayer = MediaPlayer()
    private var mArcadeSuccessEffectMediaPlayer = MediaPlayer()

    private var mSoundEffectsVolume = 0f
    private var mMainSoundVolume = 0f

    fun initAudioManager(context: Context){
        mGameMediaPlayer = MediaPlayer.create(context, R.raw.super_duper_by_ian_post)
        mGameMediaPlayer.isLooping = true

        mGameBeginsSound = MediaPlayer.create(context, R.raw.game_begin)

        mShortGameLoopSound = MediaPlayer.create(context, R.raw.game_loop_sound_short)
        mShortGameLoopSound.isLooping = true

        mLongGameLoopSound = MediaPlayer.create(context, R.raw.game_loop_sound_longer)
        mLongGameLoopSound.isLooping = true

        mTadaEffectMediaPlayer =MediaPlayer.create(context, R.raw.ta_da)

        mWawaEffectMediaPlayer = MediaPlayer.create(context,R.raw.waa_waa_waaaa)

        mBtnOnEffectMediaPlayer = MediaPlayer.create(context,R.raw.btn_on_sound)

        mBtnOffEffectMediaPlayer = MediaPlayer.create(context,R.raw.btn_on_sound)

        mArcadeSuccessEffectMediaPlayer = MediaPlayer.create(context, R.raw.success_arcade)

        mArcadeWrongAnswerEffectMediaPlayer = MediaPlayer.create(context,R.raw.wrong_answer)

        updateAudioManagerVolume()
    }

    fun updateAudioManagerVolume(){
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mMainSoundVolume = Constants.User.mainSoundLevel.toFloat()

        mGameMediaPlayer.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mGameBeginsSound.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mLongGameLoopSound.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mShortGameLoopSound.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)

        mTadaEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mWawaEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mBtnOnEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mBtnOffEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mArcadeSuccessEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mArcadeWrongAnswerEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
    }

    fun setGameVolume(mainVolume: Int) {
        mGameMediaPlayer.setVolume(mainVolume.toFloat() / 100, mainVolume.toFloat() / 100)
        updateAudioManagerVolume()
    }

    fun setEffectVolume(effectsVolume: Int) {
        mEffectsMediaPlayer.setVolume(effectsVolume.toFloat() / 100, effectsVolume.toFloat() / 100)
        updateAudioManagerVolume()
    }

    fun playGameBeginAndStartLoop()
    {
        mGameBeginsSound.start()
        mGameBeginsSound.setOnCompletionListener {
            mLongGameLoopSound.start()
        }
    }

    fun playGameBegin(){
        mGameBeginsSound.start()
    }

    fun playBtnOn() {
        mBtnOnEffectMediaPlayer.start()
    }

    fun playBtnOff() {
        mBtnOffEffectMediaPlayer.start()
    }

    fun playWaWaSound() {
        mWawaEffectMediaPlayer.start()
    }

    fun playArcadeSuccessSound() {
        mArcadeSuccessEffectMediaPlayer.start()
    }

    fun playTaDaSound() {
        mTadaEffectMediaPlayer.start()
    }

    fun playLongLoopGameMusic() {
        mLongGameLoopSound.start()
    }

    fun playWrongAnswerSound() {
        mArcadeWrongAnswerEffectMediaPlayer.start()
    }

    fun resumeLongLoopMusic() {
        mLongGameLoopSound.start()
    }
    fun stopLongLoopGameMusic() {
        mLongGameLoopSound.stop()
    }

    fun pauseLongLoopGameMusic() {
        mLongGameLoopSound.pause()
    }

    fun releaseAllMediaPlayers(){
        mGameMediaPlayer.release()

        mEffectsMediaPlayer.release()
        mTadaEffectMediaPlayer.release()
        mWawaEffectMediaPlayer.release()
        mBtnOnEffectMediaPlayer.release()
        mBtnOffEffectMediaPlayer.release()
        mArcadeWrongAnswerEffectMediaPlayer.release()
        mArcadeSuccessEffectMediaPlayer.release()
        mLongGameLoopSound.release()
        mShortGameLoopSound.release()
        mGameBeginsSound.release()
    }

}
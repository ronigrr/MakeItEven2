package com.example.makeiteven2.managers

import android.content.Context
import android.media.MediaPlayer
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.Constants

object AudioManager {


    private var mGameMediaPlayer = MediaPlayer()
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
        //mGameMediaPlayer.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mGameMediaPlayer.isLooping = true

        mTadaEffectMediaPlayer =MediaPlayer.create(context, R.raw.ta_da)
        //mTadaEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)

        mWawaEffectMediaPlayer = MediaPlayer.create(context,R.raw.waa_waa_waaaa)
        //mWawaEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)

        mBtnOnEffectMediaPlayer = MediaPlayer.create(context,R.raw.btn_on_sound)
        //mBtnOnEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)

        mBtnOffEffectMediaPlayer = MediaPlayer.create(context,R.raw.btn_on_sound)
        //mBtnOffEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)

        mArcadeSuccessEffectMediaPlayer = MediaPlayer.create(context, R.raw.success_arcade)
        //mArcadeSuccessEffectMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mArcadeWrongAnswerEffectMediaPlayer = MediaPlayer.create(context,R.raw.wrong_answer)

        updateAudioManagerVolume()
    }

    fun updateAudioManagerVolume(){
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mMainSoundVolume = Constants.User.mainSoundLevel.toFloat()

        mGameMediaPlayer.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
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

    fun playBtnOn(context: Context) {
//        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
//        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.btn_on_sound)
//        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mBtnOnEffectMediaPlayer.start()
    }

    fun playBtnOff(context: Context) {
//        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
//        mGameMediaPlayer = MediaPlayer.create(context, R.raw.btn_off_sound)
//        mGameMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mBtnOffEffectMediaPlayer.start()
    }

    fun startWaWaSound(context: Context) {
//        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
//        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.waa_waa_waaaa)
//        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mWawaEffectMediaPlayer.start()
    }

    fun startArcadeSuccessSound(context: Context) {
//        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
//        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.success_arcade)
//        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mArcadeSuccessEffectMediaPlayer.start()
    }

    fun startTaDaSound(context: Context) {
//        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
//        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.ta_da)
//        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mTadaEffectMediaPlayer.start()
    }

    fun startGameMusic(context: Context) {

//        mGameMediaPlayer = MediaPlayer.create(context, R.raw.super_duper_by_ian_post)
//        mGameMediaPlayer.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
//        mGameMediaPlayer.isLooping = true
        mGameMediaPlayer.start()
    }

    fun startWrongAnswerSound(context: Context) {
//        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
//        mEffectsMediaPlayer = MediaPlayer.create(context, R.raw.wrong_answer)
//        mEffectsMediaPlayer.setVolume(mSoundEffectsVolume, mSoundEffectsVolume)
        mArcadeWrongAnswerEffectMediaPlayer.start()
    }

    fun resumeGameMusic(context: Context) {
        mGameMediaPlayer.start()
    }

    fun stopGameMusic() {
        mGameMediaPlayer.stop()
    }

    fun pauseGameMusic() {
        mGameMediaPlayer.pause()
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
    }

}
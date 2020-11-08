package com.example.makeiteven2.managers

import android.content.Context
import android.media.MediaPlayer
import com.example.makeiteven2.R
import com.example.makeiteven2.extras.Constants
import com.example.makeiteven2.extras.SingletonHolder

class AudioManager private constructor(context: Context) {

    companion object : SingletonHolder<AudioManager, Context>(::AudioManager)

    private var mGameMediaPlayer = MediaPlayer()
    private var mGameBeginsSound = MediaPlayer()
    private var mShortGameLoopSound = MediaPlayer()
    private var mLongGameLoopSound = MediaPlayer()
    private var mEffectsMediaPlayer = MediaPlayer()
    private var mTadaEffectMediaPlayer = MediaPlayer()
    private var mWawaEffectMediaPlayer = MediaPlayer()
    private var mBtnOnEffectMediaPlayer = MediaPlayer()
    private var mBtnOffEffectMediaPlayer = MediaPlayer()
    private var mArcadeWrongAnswerEffectMediaPlayer = MediaPlayer()
    private var mArcadeSuccessEffectMediaPlayer = MediaPlayer()
    private var mStageModeGameMusic = MediaPlayer()

    private var mSoundEffectsVolume = 0f
    private var mMainSoundVolume = 0f

    init {
        mGameMediaPlayer = MediaPlayer.create(context, R.raw.super_duper_by_ian_post)
        mGameMediaPlayer.isLooping = true

        mGameBeginsSound = MediaPlayer.create(context, R.raw.game_begin)

        mShortGameLoopSound = MediaPlayer.create(context, R.raw.game_loop_sound_short)
        mShortGameLoopSound.isLooping = true

        mLongGameLoopSound = MediaPlayer.create(context, R.raw.game_loop_sound_longer)
        mLongGameLoopSound.isLooping = true

        mStageModeGameMusic = MediaPlayer.create(context, R.raw.stage_music)
        mStageModeGameMusic.isLooping = true

        mTadaEffectMediaPlayer = MediaPlayer.create(context, R.raw.ta_da)

        mWawaEffectMediaPlayer = MediaPlayer.create(context, R.raw.waa_waa_waaaa)

        mBtnOnEffectMediaPlayer = MediaPlayer.create(context, R.raw.btn_on_sound)

        mBtnOffEffectMediaPlayer = MediaPlayer.create(context, R.raw.btn_on_sound)

        mArcadeSuccessEffectMediaPlayer = MediaPlayer.create(context, R.raw.success_arcade)

        mArcadeWrongAnswerEffectMediaPlayer = MediaPlayer.create(context, R.raw.wrong_answer)

        updateAudioManagerVolume()
    }

    private fun updateAudioManagerVolume() {
        mSoundEffectsVolume = Constants.User.soundEffectsLevel.toFloat()
        mMainSoundVolume = Constants.User.mainSoundLevel.toFloat()

        mGameMediaPlayer.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mGameBeginsSound.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mLongGameLoopSound.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mShortGameLoopSound.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)
        mStageModeGameMusic.setVolume(mMainSoundVolume / 100, mMainSoundVolume / 100)

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

    fun playGameBeginAndStartLoop() {
        mGameBeginsSound.start()
        mGameBeginsSound.setOnCompletionListener {
            playLongLoopGameMusic()
        }
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
        if (!mLongGameLoopSound.isPlaying) {
            pauseCurrentLoopMusic()
            mLongGameLoopSound.start()
        }
    }

    fun playWrongAnswerSound() {
        mArcadeWrongAnswerEffectMediaPlayer.start()
    }

    fun stopLongLoopGameMusic() {
        if (mLongGameLoopSound.isPlaying) {
            mLongGameLoopSound.stop()
        }
    }

    fun pauseLongLoopGameMusic() {
        if (mLongGameLoopSound.isPlaying) {
            mLongGameLoopSound.pause()
        }
    }

    fun playStageModeLoopGameMusic(){
        if (!mStageModeGameMusic.isPlaying) {
            pauseCurrentLoopMusic()
            mStageModeGameMusic.start()
        }
    }

    fun stopStageModeLoopGameMusic(){
        if (mStageModeGameMusic.isPlaying) {
            mStageModeGameMusic.stop()
        }
    }

    fun pauseStageModeLoopGameMusic(){
        if (mStageModeGameMusic.isPlaying) {
            mStageModeGameMusic.pause()
        }
    }

    fun pauseCurrentLoopMusic(){
        if (mStageModeGameMusic?.isPlaying)
        {
            pauseStageModeLoopGameMusic()
        }
        else if (mLongGameLoopSound?.isPlaying)
        {
            pauseLongLoopGameMusic()
        }
    }

    fun stopCurrentLoopMusic(){
        if (mStageModeGameMusic.isPlaying)
        {
            stopStageModeLoopGameMusic()
        }
        else if (mLongGameLoopSound.isPlaying)
        {
            stopLongLoopGameMusic()
        }
    }

    fun playLoopMusicForSpecificFragment(currentFragmentTag: String) {
        when(currentFragmentTag){
            Constants.START_SCREEN_FRAGMENT_TAG->playLongLoopGameMusic()
            Constants.LEVELS_SCREEN_FRAGMENT_TAG->playStageModeLoopGameMusic()
            Constants.ARCADE_MODE_SCREEN_FRAGMENT_TAG->playStageModeLoopGameMusic()
            Constants.SETTINGS_SCREEN_FRAGMENT_TAG->playLongLoopGameMusic()
            Constants.STAGE_MODE_SCREEN_FRAGMENT_TAG->playStageModeLoopGameMusic()
            Constants.SCOREBOARD_SCREEN_FRAGMENT_TAG->playLongLoopGameMusic()
        }
    }
    fun releaseAllMediaPlayers() {
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
        mStageModeGameMusic.release()
    }
}
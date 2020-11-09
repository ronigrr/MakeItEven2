package com.yoyoG.makeiteven2.intefaces

interface IFragmentSettingsListener {
    fun onSeekBarMainVolume(mainVolume: Int)
    fun onSeekBarSoundEffects(soundEffectsVolume: Int)
    fun onResetGame()
    fun onExitFromSettingsFragment()
}
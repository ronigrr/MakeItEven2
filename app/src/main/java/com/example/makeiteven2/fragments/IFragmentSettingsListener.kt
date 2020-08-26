package com.example.makeiteven2.fragments

interface IFragmentSettingsListener {
    fun onSeekBarMainVolume(mainVolume: Int)
    fun onSeekBarSoundEffects(soundEffectsVolume: Int)
    fun onResetGame()
    fun onExitFromSettingsFragment()
}
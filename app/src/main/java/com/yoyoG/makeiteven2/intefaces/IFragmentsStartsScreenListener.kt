package com.yoyoG.makeiteven2.intefaces

import android.view.View

interface IFragmentsStartsScreenListener {
    fun onStartScreenFragmentButtonClicked(view: View)
    fun show3DotsToolBar()
    fun hideNaveBarFromStartScreen()
}
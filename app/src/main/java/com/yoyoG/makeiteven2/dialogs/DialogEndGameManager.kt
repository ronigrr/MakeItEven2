package com.yoyoG.makeiteven2.dialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import androidx.core.text.set
import com.yoyoG.makeiteven2.R
import com.yoyoG.makeiteven2.extras.Constants
import com.yoyoG.makeiteven2.firebase.FireBaseHelper
import com.yoyoG.makeiteven2.intefaces.IEndDialogBtnClickedListener
import com.yoyoG.makeiteven2.managers.AnimationsManager
import com.yoyoG.makeiteven2.managers.GoogleAdManager
import com.yoyoG.makeiteven2.room.DatabaseHelper
import kotlinx.android.synthetic.main.win_loose_dialog.*

class DialogEndGameManager(fragment: Any, private val mContext: Context) {

    private val listener: IEndDialogBtnClickedListener
    private var winLoseDialog: Dialog = Dialog(mContext)

    init {
        if (fragment is IEndDialogBtnClickedListener) {
            listener = fragment
        } else {
            throw ClassCastException(fragment.toString() + "must implement IEndDialogBtnClicked")
        }
    }

    fun arcadeDismissDialog() {
        if (winLoseDialog.isShowing) {
            checkAndSaveName()
            winLoseDialog.dismiss()
        }
    }

    private fun checkAndSaveName() {
        if (winLoseDialog.etNickname.text.isNotEmpty()
            && winLoseDialog.etNickname.text.toString() != Constants.User.playerName)
        {
            DatabaseHelper.changePlayerNickname(mContext,winLoseDialog.etNickname.text.toString())
            FireBaseHelper.updateScoreBoardUserNickName(winLoseDialog.etNickname.text.toString())
        }

    }

    private fun checkAndShowAdd(levelNum : Int){
        val addMobRate = when(Constants.User.currentLevel) {
            in 1..20 -> 8
            in 20..40 -> 6
            else -> 5
        }
        if (levelNum % addMobRate == 0) {
            GoogleAdManager.showInterstitialAd()
        }
    }

    fun stageModeDismissDialogWithAd(adControlParameter : Int ){
        winLoseDialog.dismiss()
        checkAndShowAdd(adControlParameter)
    }

    fun showEndDialog(whichDialog: String, score: String = "") {

        winLoseDialog = Dialog(mContext)

        winLoseDialog.setCanceledOnTouchOutside(false)
        winLoseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        winLoseDialog.setCancelable(false)
        winLoseDialog.setContentView(R.layout.win_loose_dialog)
        winLoseDialog.ibtnHome.setOnTouchListener(AnimationsManager.getInstance(mContext).getTouchAnimation())
        winLoseDialog.ibtnNext.setOnTouchListener(AnimationsManager.getInstance(mContext).getTouchAnimation())
        winLoseDialog.ibtnRetry.setOnTouchListener(AnimationsManager.getInstance(mContext).getTouchAnimation())
        winLoseDialog.setOnDismissListener { listener.onDialogDismiss() }

        winLoseDialog.ibtnHome.setOnClickListener { listener.onEndDialogBtnClicked(it) }
        winLoseDialog.ibtnNext.setOnClickListener { listener.onEndDialogBtnClicked(it) }
        winLoseDialog.ibtnRetry.setOnClickListener { listener.onEndDialogBtnClicked(it) }
        winLoseDialog.ibtnScoreBoard.setOnClickListener { listener.onEndDialogBtnClicked(it) }

        when (whichDialog) {
            Constants.WIN_DIALOG -> {
                winLoseDialog.tvText.text = mContext.resources.getString(R.string.correct_answer)
                winLoseDialog.animationView.setAnimation(R.raw.win_owl_anim)
                winLoseDialog.animationView.playAnimation()
                winLoseDialog.ibtnScoreBoard.visibility = View.GONE
            }
            Constants.LOSE_DIALOG -> {
                winLoseDialog.ibtnNext.visibility = View.GONE
                winLoseDialog.ibtnScoreBoard.visibility = View.GONE
                winLoseDialog.tvText.text = mContext.resources.getString(R.string.wrong_answer)
                winLoseDialog.animationView.setAnimation(R.raw.loose_anim)
                winLoseDialog.animationView.playAnimation()
            }
            Constants.ARCADE_END_DIALOG -> {
                winLoseDialog.ibtnNext.visibility = View.GONE
                winLoseDialog.tvText.text = "${mContext.getString(R.string.you_score_is)} $score"

                winLoseDialog.etNickname.visibility = View.VISIBLE
                winLoseDialog.etNickname.setText(Constants.User.playerName)

                winLoseDialog.animationView.setAnimation(R.raw.times_up)
                winLoseDialog.animationView.playAnimation()
            }
        }
        winLoseDialog.show()
    }
}

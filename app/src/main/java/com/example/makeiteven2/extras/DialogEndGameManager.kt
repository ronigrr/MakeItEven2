package com.example.makeiteven2.extras

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import com.example.makeiteven2.R
import com.example.makeiteven2.intefaces.IEndDialogBtnClickedListener
import kotlinx.android.synthetic.main.win_loose_dialog.*

class DialogEndGameManager(fragment: Any,private val mContext: Context) {

    private val listener: IEndDialogBtnClickedListener
    private lateinit var winLooseDialog: Dialog
    init {
        if (fragment is IEndDialogBtnClickedListener) {
            listener = fragment
        } else {
            throw ClassCastException(fragment.toString() + "must implement IEndDialogBtnClicked")
        }
    }

    fun dismissDialog(){
        winLooseDialog.dismiss()
    }
    fun shodEndDialog(whichDialog : String,score : String = ""){
        winLooseDialog = Dialog(mContext)

        winLooseDialog.setCanceledOnTouchOutside(false)
        winLooseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        winLooseDialog.setCancelable(false)
        winLooseDialog.setContentView(R.layout.win_loose_dialog)
        winLooseDialog.ibtnHome.setOnTouchListener(Animations.getTouchAnimation(mContext))
        winLooseDialog.ibtnNext.setOnTouchListener(Animations.getTouchAnimation(mContext))
        winLooseDialog.ibtnRetry.setOnTouchListener(Animations.getTouchAnimation(mContext))

        winLooseDialog.ibtnHome.setOnClickListener { listener.onEndDialogBtnClicked(it) }
        winLooseDialog.ibtnNext.setOnClickListener {   listener.onEndDialogBtnClicked(it)}
        winLooseDialog.ibtnRetry.setOnClickListener {  listener.onEndDialogBtnClicked(it) }
        winLooseDialog.ibtnScoreBoard.setOnClickListener { listener.onEndDialogBtnClicked(it) }

        when (whichDialog) {
            Constants.WIN_DIALOG -> {
                winLooseDialog.tvText.text = mContext.resources.getString(R.string.correct_answer)
                winLooseDialog.animationView.setAnimation(R.raw.win_owl_anim)
                winLooseDialog.animationView.playAnimation()
                winLooseDialog.ibtnScoreBoard.visibility = View.GONE
            }
            Constants.LOSE_DIALOG -> {
                winLooseDialog.ibtnNext.visibility = View.GONE
                winLooseDialog.ibtnScoreBoard.visibility = View.GONE
                winLooseDialog.tvText.text = mContext.resources.getString(R.string.wrong_answer)
                winLooseDialog.animationView.setAnimation(R.raw.loose_anim)
                winLooseDialog.animationView.playAnimation()
            }
            Constants.ARCADE_END_DIALOG->{
                winLooseDialog.ibtnNext.visibility = View.GONE
                winLooseDialog.tvText.text = "${mContext.getString(R.string.you_score_is)} $score"
                winLooseDialog.animationView.setAnimation(R.raw.times_up)
                winLooseDialog.animationView.playAnimation()
            }
        }
        winLooseDialog.show()
        }
    }

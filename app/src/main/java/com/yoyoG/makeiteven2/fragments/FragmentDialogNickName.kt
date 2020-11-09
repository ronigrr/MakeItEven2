package com.yoyoG.makeiteven2.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.yoyoG.makeiteven2.R
import com.yoyoG.makeiteven2.extras.Constants
import kotlinx.android.synthetic.main.nickname_dialog.view.*

class FragmentDialogNickName : DialogFragment() {

    interface DialogListener {
        fun onFinishEditDialog(inputText: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (arguments != null) {
            if (arguments?.getBoolean("notAlertDialog")!!) {
                return super.onCreateDialog(savedInstanceState)
            }
        }
        val builder = AlertDialog.Builder(activity)
        builder.setPositiveButton("Cool", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dismiss()
            }
        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, which: Int) {
                dismiss()
            }
        })
        return builder.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.nickname_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nicknameEt = view.findViewById<EditText>(R.id.etNickname)
        if (arguments != null && !TextUtils.isEmpty(arguments?.getString(Constants.NICK_NAME_kYE)))
            nicknameEt.setText(arguments?.getString(Constants.NICK_NAME_kYE))

        view.btnDone.setOnClickListener {
            if (nicknameEt.text.length > 16) {
                Toast.makeText(view.context, "Name is to long", Toast.LENGTH_SHORT).show()
            }
            if (nicknameEt.text.isEmpty()) {
                Toast.makeText(view.context, "You must chose name or nickname", Toast.LENGTH_SHORT).show()
            } else {
                val dialogListener = activity as DialogListener
                dialogListener.onFinishEditDialog(nicknameEt.text.toString())
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var setFullScreen = false
        if (arguments != null) {
            setFullScreen = requireNotNull(arguments?.getBoolean("fullScreen"))
        }
        if (setFullScreen)
            setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }
}
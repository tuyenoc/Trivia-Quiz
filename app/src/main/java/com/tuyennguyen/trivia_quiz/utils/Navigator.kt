package com.tuyennguyen.trivia_quiz.utils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tuyennguyen.trivia_quiz.R

object Navigator {
    fun push(fragmentManager: FragmentManager, fragment: Fragment, tag: String?) {
        fragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }

    fun pop(fragmentManager: FragmentManager) {
        fragmentManager.popBackStack()
    }

    fun pop(fragmentManager: FragmentManager, name: String?) {
        fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun buildDialogInProgress(context: Context): Dialog {
        val dialog = Dialog(context, R.style.DialogInProgress)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.setContentView(R.layout.dialog_in_progress)
        return dialog

    }

    fun buildDialogCongratulations(context: Context, fragmentManager: FragmentManager): Dialog {
        val dialog = Dialog(context, R.style.DialogAlert)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_congratulations, null, false)
        val btBack = view.findViewById<Button>(R.id.btBack)
        btBack.setOnClickListener {
            dialog.dismiss()
            pop(fragmentManager)
        }
        dialog.setContentView(view)
        return dialog
    }

    fun buildDialogLose(context: Context, fragmentManager: FragmentManager): Dialog {
        val dialog = Dialog(context, R.style.DialogAlert)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_lose, null, false)
        val btBack = view.findViewById<Button>(R.id.btBack)
        btBack.setOnClickListener {
            dialog.dismiss()
            pop(fragmentManager)
        }
        dialog.setContentView(view)
        return dialog
    }

}

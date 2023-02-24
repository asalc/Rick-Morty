package com.shiro.arturosalcedogagliardi.helpers.extensions

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.shiro.arturosalcedogagliardi.databinding.CustomDialogBinding
import com.shiro.arturosalcedogagliardi.helpers.Constants

fun Context.showDoubleDialog(
    data: Map<String, *>,
    onAccept: () -> Unit
): Dialog {
    val binding = CustomDialogBinding.inflate(
        LayoutInflater.from(this)
    )

    if (data.containsKey(Constants.DIALOG_TITLE))
        data.getValue(Constants.DIALOG_TITLE)?.cast<String>()?.let {
            binding.textTitle.text = it
        }

    if (data.containsKey(Constants.DIALOG_DESCRIPTION))
        data.getValue(Constants.DIALOG_DESCRIPTION)?.cast<String>()?.let {
            binding.textDescription.text = it.fromHtml()
        }

    val dialog = Dialog(this)
    dialog.setCancelable(false)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    binding.buttonAccept.setOnClickListener {
        dialog.dismiss()
        onAccept()
    }

    dialog.setCanceledOnTouchOutside(false)
    dialog.setCancelable(false)
    dialog.show()
    return dialog
}

fun Context.hideKeyboard(view: View?) {
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}


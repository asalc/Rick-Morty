package com.shiro.arturosalcedogagliardi.helpers.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.shiro.arturosalcedogagliardi.databinding.CustomDialogBinding
import com.shiro.arturosalcedogagliardi.helpers.Constants

val Context.isNetworkAvailable: Boolean
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

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
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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


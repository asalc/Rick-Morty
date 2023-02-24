package com.shiro.arturosalcedogagliardi.helpers.extensions

import android.text.Html
import android.text.Spanned

fun String.fromHtml(): Spanned? =
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)

fun String.capitalize(): String {
    val stringSplit = this.split(" ")
    return if (stringSplit.size <= 1)
        this.replaceFirstChar { it.uppercase() }
    else
        stringSplit.onEach {
            it.uppercase()
        }.joinToString(" ")
}
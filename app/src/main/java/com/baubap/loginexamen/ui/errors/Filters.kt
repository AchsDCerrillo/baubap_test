package com.baubap.loginexamen.ui.errors

fun String.filterSize(size: Int): String =
    if (this.length > size) this.substring(0 until size)
    else this
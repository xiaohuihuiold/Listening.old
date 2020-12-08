package com.xhhold.media.ext

import android.os.Bundle
import android.os.Parcelable

fun <T : Parcelable?> Bundle.getData(): T? = getParcelable<T?>("data")

fun Bundle.setData(parcelable: Parcelable?): Bundle {
    putParcelable("data", parcelable)
    return this
}
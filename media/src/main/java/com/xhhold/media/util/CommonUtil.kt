package com.xhhold.media.util

import com.xhhold.media.entity.BaseTime

object CommonUtil {
    fun getTime(): BaseTime =
        BaseTime(System.currentTimeMillis(), System.currentTimeMillis())
}
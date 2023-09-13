package com.laelar.app.assets

import com.laelar.core.models.Block
import com.naulian.anhance.formatWith
import com.naulian.anhance.millisOfNow

object Injection {
    private val dateExample get() = millisOfNow.formatWith("yyyy-MM-dd HH:mm:ss.SSSSSS")
    val map = mapOf(
        "date_example" to Block(
            subTitle = "Now",
            body = "ယခုလက်ရှိအချိန်ကိုယူဖို့ datetime module ရဲ့ datetime.now() function ကိုသုံးပါ။",
            code = "import datetime\n\nnow = datetime.datetime.now()\nprint(now)\n# output\n# $dateExample",
            language = "python"
        )
    )
}
package com.todayseyebrow.commons.extensions

import android.app.Application
import com.todayseyebrow.commons.helpers.isNougatPlus
import java.util.*

fun Application.checkUseEnglish() {
    if (baseConfig.useEnglish && !isNougatPlus()) {
        val conf = resources.configuration
        conf.setLocale(Locale.ENGLISH)
        createConfigurationContext(conf)
    }
}

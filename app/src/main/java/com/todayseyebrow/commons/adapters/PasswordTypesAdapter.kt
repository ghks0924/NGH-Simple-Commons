package com.todayseyebrow.commons.adapters

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.auth.AuthPromptHost
import androidx.viewpager.widget.PagerAdapter
import com.todayseyebrow.commons.R
import com.todayseyebrow.commons.extensions.isTargetSdkVersion30Plus
import com.todayseyebrow.commons.helpers.PROTECTION_FINGERPRINT
import com.todayseyebrow.commons.helpers.PROTECTION_PATTERN
import com.todayseyebrow.commons.helpers.PROTECTION_PIN
import com.todayseyebrow.commons.interfaces.HashListener
import com.todayseyebrow.commons.interfaces.SecurityTab
import com.todayseyebrow.commons.views.MyScrollView

class PasswordTypesAdapter(
    private val context: Context,
    private val requiredHash: String,
    private val hashListener: HashListener,
    private val scrollView: MyScrollView,
    private val biometricPromptHost: AuthPromptHost,
    private val showBiometricIdTab: Boolean
) : PagerAdapter() {
    private val tabs = SparseArray<SecurityTab>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(layoutSelection(position), container, false)
        container.addView(view)
        tabs.put(position, view as SecurityTab)
        (view as SecurityTab).initTab(requiredHash, hashListener, scrollView, biometricPromptHost)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        tabs.remove(position)
        container.removeView(item as View)
    }

    override fun getCount() = if (showBiometricIdTab) 3 else 2

    override fun isViewFromObject(view: View, item: Any) = view == item

    private fun layoutSelection(position: Int): Int = when (position) {
        PROTECTION_PATTERN -> R.layout.tab_pattern
        PROTECTION_PIN -> R.layout.tab_pin
        PROTECTION_FINGERPRINT -> if (context.isTargetSdkVersion30Plus()) R.layout.tab_biometric_id else R.layout.tab_fingerprint
        else -> throw RuntimeException("Only 3 tabs allowed")
    }

    fun isTabVisible(position: Int, isVisible: Boolean) {
        tabs[position]?.visibilityChanged(isVisible)
    }
}

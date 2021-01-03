package com.ulusoyapps.venucity.main.ui

import android.content.Context
import android.util.AttributeSet
import com.airbnb.epoxy.EpoxyRecyclerView

/**
 * [EpoxyRecyclerView] that makes sure adapter reference is removed straight away [onDetachedFromWindow]
 * You may not want to use this for nested RecyclerViews.
 * see. https://github.com/airbnb/epoxy/wiki/Avoiding-Memory-Leaks
 */
class AutoClearEpoxyRecyclerView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : EpoxyRecyclerView(context, attrs, defStyleAttr) {
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter = null
    }
}

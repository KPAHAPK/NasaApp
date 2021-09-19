package com.example.nasaapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class ViewBelowAppBarBehavior @JvmOverloads constructor(
    context: Context?, attr: AttributeSet? = null
) : CoordinatorLayout.Behavior<View>(context, attr) {

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean = dependency is AppBarLayout

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val appBar = dependency as AppBarLayout
        val currentAppBarHeight = appBar.height + appBar.y
        val parentHeight = parent.height
        val placeHolderHeight = (parentHeight - currentAppBarHeight).toInt()
        child.layoutParams?.height = placeHolderHeight
        child.requestLayout()
        return false
    }
}
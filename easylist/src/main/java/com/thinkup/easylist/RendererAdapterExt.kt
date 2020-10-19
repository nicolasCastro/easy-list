package com.thinkup.easylist

import android.content.Context
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.View.GONE
import android.view.animation.Animation
import androidx.recyclerview.widget.RecyclerView

private const val DISABLE_VALUE = 0f

fun RendererAdapter.removeItemAnimator(
    recyclerView: RecyclerView,
    item: Any,
    animation: Animation
) {
    val adapter = this
    val indexDelete = adapter.getItems().indexOf(item)
    if (indexDelete != -1) {
        val view: View? = recyclerView.findViewHolderForAdapterPosition(indexDelete)?.itemView
        if (view != null && isAnimationEnabled(view.context)) {
            view.startAnimation(animation)
            Handler().postDelayed(
                {
                    view.visibility = GONE
                    adapter.removeItem(indexDelete)
                },
                animation.duration
            )
        } else {
            adapter.removeItem(indexDelete)
        }
    }
}

private fun isAnimationEnabled(context: Context?): Boolean =
    context?.let {
        Settings.Global.getFloat(
            it.contentResolver,
            Settings.Global.TRANSITION_ANIMATION_SCALE,
            DISABLE_VALUE
        ) != DISABLE_VALUE && Settings.Global.getFloat(
            it.contentResolver,
            Settings.Global.WINDOW_ANIMATION_SCALE,
            DISABLE_VALUE
        ) != DISABLE_VALUE
    } ?: false
package com.example.tah.ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

class ViewAnimations {

    companion object {
        private const val shortDuration = 300L

        fun show(vararg view: View) {
            for (v in view) {
                v.apply {
                    alpha = 0f
                    visibility = View.GONE

                    animate()
                        .alpha(1f)
                        .setDuration(shortDuration)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                visibility = View.VISIBLE
                            }
                        })
                }
            }
        }

        fun hide(vararg view: View) {
            for(v in view) {
                v.apply {
                    alpha = 1f
                    visibility = View.VISIBLE

                    animate()
                        .alpha(0f)
                        .setDuration(shortDuration)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                visibility = View.GONE
                            }
                        })
                }
            }
        }

    }
}
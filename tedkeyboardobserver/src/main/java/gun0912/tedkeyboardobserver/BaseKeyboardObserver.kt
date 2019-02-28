package gun0912.tedkeyboardobserver

import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.ViewTreeObserver

abstract class BaseKeyboardObserver(private val activity: Activity) {

    private val decorView = activity.window.decorView

    private val onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener =
        ViewTreeObserver.OnGlobalLayoutListener { onGlobalLayout() }

    private var lastVisibleDecorViewHeight = 0

    private val softKeyButtonHeight = {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val usableHeight = displayMetrics.heightPixels
        activity.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        val realHeight = displayMetrics.heightPixels
        if (realHeight > usableHeight)
            realHeight - usableHeight
        else
            0
    }()

    private var minKeyboardHeightPx = softKeyButtonHeight


    private lateinit var onKeyboardListener: OnKeyboardListener

    private fun getWindowHeight() =
        Rect().apply { decorView.getWindowVisibleDisplayFrame(this) }.height()

    private fun registerActivityLifecycleCallbacks() {
        activity.application.registerActivityLifecycleCallbacks(object :
            SimpleActivityLifecycleCallbacks(activity) {
            override fun onActivityCreated() {
                this@BaseKeyboardObserver.onActivityCreated()
            }

            override fun onActivityDestroyed() {
                this@BaseKeyboardObserver.onActivityDestroyed()
            }
        })
    }

    private fun onGlobalLayout() {
        val visibleDecorViewHeight = getWindowHeight()
        // Decide whether keyboard is visible from changing decor view height.
        if (lastVisibleDecorViewHeight != 0) {
            if (lastVisibleDecorViewHeight > visibleDecorViewHeight + minKeyboardHeightPx) {
                // Notify listener about keyboard being shown.
                minKeyboardHeightPx =
                    ((lastVisibleDecorViewHeight - visibleDecorViewHeight) * 0.9).toInt() - softKeyButtonHeight
                onKeyboardListener.onKeyboardChange(true)
            } else if (lastVisibleDecorViewHeight + minKeyboardHeightPx < visibleDecorViewHeight) {
                // Notify listener about keyboard being hidden.
                onKeyboardListener.onKeyboardChange(false)
            }
        }
        // Save current decor view height for the next call.
        lastVisibleDecorViewHeight = visibleDecorViewHeight
    }


    protected fun internalListen(onKeyboardListener: OnKeyboardListener) {
        registerActivityLifecycleCallbacks()
        this.onKeyboardListener = onKeyboardListener
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    protected open fun onActivityCreated() {}

    protected open fun onActivityDestroyed() {
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    interface OnKeyboardListener {
        fun onKeyboardChange(isShow: Boolean)
    }
}
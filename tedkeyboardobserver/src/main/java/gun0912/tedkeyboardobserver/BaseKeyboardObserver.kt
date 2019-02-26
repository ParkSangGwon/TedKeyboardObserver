package gun0912.tedkeyboardobserver

import android.app.Activity
import android.graphics.Rect
import android.view.ViewTreeObserver

abstract class BaseKeyboardObserver(private val activity: Activity) {

    private val decorView = activity.window.decorView

    private lateinit var onKeyboardListener: OnKeyboardListener
    private var originalWindowHeight = getWindowHeight()
    private fun getWindowHeight() = Rect().apply { decorView.getWindowVisibleDisplayFrame(this) }.bottom
    private val onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener =
        ViewTreeObserver.OnGlobalLayoutListener { onGlobalLayout() }

    private fun registerActivityLifecycleCallbacks() {
        activity.application.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks(activity) {
            override fun onActivityCreated() {
                this@BaseKeyboardObserver.onActivityCreated()
            }

            override fun onActivityDestroyed() {
                this@BaseKeyboardObserver.onActivityDestroyed()
            }
        })
    }

    protected fun internalListen(onKeyboardListener: OnKeyboardListener) {
        registerActivityLifecycleCallbacks()
        this.onKeyboardListener = onKeyboardListener
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }


    private fun onGlobalLayout() {
        val currentWindowHeight = getWindowHeight()
        val isShow = originalWindowHeight != currentWindowHeight
        onKeyboardListener.onKeyboardChange(isShow)
    }

    protected open fun onActivityCreated() {
        originalWindowHeight = getWindowHeight()
    }

    protected open fun onActivityDestroyed() {
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    interface OnKeyboardListener {
        fun onKeyboardChange(isShow: Boolean)
    }
}
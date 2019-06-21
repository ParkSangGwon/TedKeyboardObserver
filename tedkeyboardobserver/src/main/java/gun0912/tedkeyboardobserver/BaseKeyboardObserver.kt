package gun0912.tedkeyboardobserver

import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.ViewTreeObserver
import kotlin.math.absoluteValue

abstract class BaseKeyboardObserver(private val activity: Activity) {

    private val decorView = activity.window.decorView
    private var lastIsShow = false

    private lateinit var onKeyboardListener: OnKeyboardListener
    private val originalWindowHeight by lazy { getWindowHeight() }
    private fun getWindowHeight() = Rect().apply { decorView.getWindowVisibleDisplayFrame(this) }.bottom
    private val onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener =
        ViewTreeObserver.OnGlobalLayoutListener { onGlobalLayout() }

    private var lastWindowHeight = getWindowHeight()
    private val softKeyButtonHeight = {
        val applicationDisplayHeight = DisplayMetrics().apply {
            activity.windowManager.defaultDisplay.getMetrics(this)
        }.heightPixels
        val realDisplayHeight = DisplayMetrics().apply {
            activity.windowManager.defaultDisplay.getRealMetrics(this)
        }.heightPixels

        realDisplayHeight - applicationDisplayHeight
    }()

    private fun registerActivityLifecycleCallbacks() {
        activity.application.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks(activity) {
            override fun onActivityCreated() {
               // no-op
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
        if (isSoftKeyChanged()) {
            lastWindowHeight = currentWindowHeight
            return
        }
        lastWindowHeight = currentWindowHeight
        val isShow = originalWindowHeight != currentWindowHeight
        if (lastIsShow == isShow) {
            return
        }
        lastIsShow = isShow
        onKeyboardListener.onKeyboardChange(isShow)
    }

    private fun isSoftKeyChanged() = ((lastWindowHeight - getWindowHeight()).absoluteValue) == softKeyButtonHeight

    protected open fun onActivityDestroyed() {
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    interface OnKeyboardListener {
        fun onKeyboardChange(isShow: Boolean)
    }
}
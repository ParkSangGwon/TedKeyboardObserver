package gun0912.tedkeyboardobserver

import android.app.Activity

class TedKeyboardObserver(activity: Activity) : BaseKeyboardObserver(activity) {

    fun listen(onKeyboardListener: OnKeyboardListener) {
        internalListen(onKeyboardListener)
    }

    fun listen(action: (Boolean) -> Unit) {
        internalListen(object : OnKeyboardListener {
            override fun onKeyboardChange(isShow: Boolean) {
                action(isShow)
            }
        })
    }
}
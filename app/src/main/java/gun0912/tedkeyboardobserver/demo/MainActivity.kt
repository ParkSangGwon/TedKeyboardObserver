package gun0912.tedkeyboardobserver.demo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import gun0912.tedkeyboardobserver.TedKeyboardObserver
import gun0912.tedkeyboardobserver.TedRxKeyboardObserver

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStatusNormal = findViewById<TextView>(R.id.tv_status_normal)
        TedKeyboardObserver(this)
            .listen {
                tvStatusNormal.text = "[Normal]Keyboard show: $it"
            }


        val tvStatusRx = findViewById<TextView>(R.id.tv_status_rx)

        // You don't need dispose this observable.
        // When activity destroy, TedRxKeyboardObserver will call onComplete()
        TedRxKeyboardObserver(this)
            .listen()
            .subscribe({ isShow -> tvStatusRx.text = "[Rx]Keyboard show: $isShow" }
                , { throwable -> throwable.printStackTrace() }
                , { Log.d("ted", "onComplete()") })
    }
}

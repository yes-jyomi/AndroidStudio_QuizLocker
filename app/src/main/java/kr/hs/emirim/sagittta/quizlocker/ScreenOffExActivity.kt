package kr.hs.emirim.sagittta.quizlocker

import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ScreenOffExActivity : AppCompatActivity() {

    var screenOffReceiver: ScreenOffReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_off_ex)

        if (screenOffReceiver == null) {
            screenOffReceiver = ScreenOffReceiver()

            val intentFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
            registerReceiver(screenOffReceiver, intentFilter)
        }
    }
}

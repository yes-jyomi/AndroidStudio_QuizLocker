package kr.hs.emirim.sagittta.quizlocker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceFragment

class PrefFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pref_fragment)

        fragmentManager.beginTransaction().replace(android.R.id.content, MyPrefFragment()).commit()
    }

    class MyPrefFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            addPreferencesFromResource(R.xml.ex_pref)
        }
    }
}

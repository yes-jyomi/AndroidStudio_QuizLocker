package kr.hs.emirim.sagittta.quizlocker

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.WindowManager
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_quiz_locker.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class QuizLockerActivity : AppCompatActivity() {

    var quiz:JSONObject? = null

    val wrongAnswerPref by lazy {
        getSharedPreferences("wrongAnswer", Context.MODE_PRIVATE)
    }

    val correctAnswerPref by lazy {
        getSharedPreferences("correctAnswer", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)

            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)

            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_quiz_locker)

        val json = assets.open("capital.json").reader().readText()
        val quizArray = JSONArray(json)

        quiz = quizArray.getJSONObject(Random().nextInt(quizArray.length()))

        quizLabel.text = quiz?.getString("question")
        choice1.text = quiz?.getString("choice1")
        choice2.text = quiz?.getString("choice2")

        val id = quiz?.getInt("id").toString() ?: ""
        correctCountLabel.text = "정답횟수:${correctAnswerPref.getInt(id, 0)}"
        wrongCountLabel.text = "오답횟수:${wrongAnswerPref.getInt(id, 0)}"

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                when {
                    progress > 95 -> {
                        leftImageView.setImageResource(R.drawable.padlock)
                        rightImageView.setImageResource(R.drawable.unlock)
                    }

                    progress < 5 -> {
                        leftImageView.setImageResource(R.drawable.unlock)
                        rightImageView.setImageResource(R.drawable.padlock)
                    }

                    else -> {
                        leftImageView.setImageResource(R.drawable.padlock)
                        rightImageView.setImageResource(R.drawable.padlock)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress ?: 50

                when {
                    progress > 95 -> checkChoice(quiz?.getString("choice2") ?: "")

                    progress < 5 -> checkChoice(quiz?.getString("choice1") ?: "")

                    else -> seekBar?.progress = 50
                }
            }
        })
    }

    fun checkChoice(choice:String) {
        quiz?.let {
            when {
                choice == it.getString("answer") -> {
                    val id = it.getInt("id").toString()
                    var count = correctAnswerPref.getInt(id, 0)
                    count++

                    correctAnswerPref.edit().putInt(id, count).apply()
                    correctCountLabel.text = "정답횟수: ${count}"

                    finish()
                }
                else -> {
                    val id = it.getInt("id").toString()
                    var count = wrongAnswerPref.getInt(id, 0)
                    count++
                    wrongAnswerPref.edit().putInt(id, count).apply()
                    wrongCountLabel.text = "오답횟수: ${count}"

                    leftImageView.setImageResource(R.drawable.padlock)
                    rightImageView.setImageResource(R.drawable.padlock)
                    seekBar?.progress = 50

                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createOneShot(1000, 100))
                    } else {
                        vibrator.vibrate(1000)
                    }
                }
            }
        }
    }
}

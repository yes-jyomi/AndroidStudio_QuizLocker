package kr.hs.emirim.sagittta.quizlocker

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_file_ex.*
import java.io.FileNotFoundException

class FileExActivity : AppCompatActivity() {

    val filename = "data.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_ex)

        saveButton.setOnClickListener {
            val text = textField.text.toString()
            when {
                TextUtils.isEmpty(text) -> {
                    Toast.makeText(applicationContext, "텍스트가 비어 있습니다.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    saveToInnerStorage(text, filename)
                }
            }
        }

        loadButton.setOnClickListener {
            try {
                textField.setText(loadFromInnerStorage(filename))
            } catch (e: FileNotFoundException) {
                Toast.makeText(applicationContext, "저장된 텍스트가 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveToInnerStorage(text: String, filename: String) {
        val fileOutPutStream = openFileOutput(filename, Context.MODE_PRIVATE)
        fileOutPutStream.write(text.toByteArray())
        fileOutPutStream.close()
    }

    private fun loadFromInnerStorage(filename: String): String {
        val fileInputStream = openFileInput(filename)

        return fileInputStream.reader().readText()
    }

}

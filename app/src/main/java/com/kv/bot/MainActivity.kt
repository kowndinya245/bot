package com.kv.bot

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.widget.Toast
import com.kv.bot.datastorage.dataStorage
import com.kv.bot.datastorage.dataStorage.USERNAME
import com.kv.bot.datastorage.dataStorage.loadData
import com.kv.bot.datastorage.dataStorage.saveData
import com.kv.bot.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.myTextField.setText(loadData(applicationContext, USERNAME, ""))
        if (supportActionBar != null) {
            supportActionBar!!.hide()
            // Set the system UI visibility and status bar color
            window?.decorView?.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun isInputValid(): Boolean {
        val input = binding.myTextField.text.trim()
        return input.isNotEmpty()
    }

    fun startChat(view: View) {
        if (isInputValid()) {
            saveData(applicationContext, USERNAME, binding.myTextField.text.toString())
            startActivity(Intent(applicationContext, ChatActivity::class.java))
        } else {
            Toast.makeText(applicationContext, "Enter a name", Toast.LENGTH_LONG).show()
        }
    }
}

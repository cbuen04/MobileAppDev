package com.example.stand_alone_app

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.stand_alone_app.databinding.ActivityHomepageBinding
import com.google.android.material.textview.MaterialTextView

class homepage : AppCompatActivity() {

    private var homeFirstName: String? = null
    private var homeLastName: String? = null
    private var hWelcome: String? = null

    private var hTvWelcome: MaterialTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_homepage)

        val recvdIntent = intent
        homeFirstName = recvdIntent.getStringExtra("FIRST_NAME")
        homeLastName = recvdIntent.getStringExtra("LAST_NAME")

        hTvWelcome = findViewById(R.id.home_welcome_prompt)

        hTvWelcome!!.text = "Welcome," + "\n" + homeFirstName + " " + homeLastName + "!"


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        hWelcome = hTvWelcome!!.text.toString()

        outState.putString("WELCOME_PROMPT", hWelcome)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        hTvWelcome!!.text = savedInstanceState.getString("WELCOME_PROMPT")
    }

}
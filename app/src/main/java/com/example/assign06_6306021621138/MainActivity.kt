package com.example.assign06_6306021621138

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var spn_gameMode: Spinner
    private lateinit var main_btnPlay: Button
    private lateinit var main_btnExit: Button
    private val gameModes: List<String> = listOf("Easy", "Normal", "Hard")
    private var gameMode: Int = 0

    private fun init() {
        this.initChoiceGameMode()

        main_btnPlay = findViewById(R.id.main_btnPlay)
        main_btnPlay.setOnClickListener(this)

        main_btnExit = findViewById(R.id.main_btnExit)
        main_btnExit.setOnClickListener(this)
    }


    private fun initChoiceGameMode() {
        spn_gameMode = findViewById(R.id.spn_gameMode)
        spn_gameMode.onItemSelectedListener = this

        spn_gameMode.adapter = ArrayAdapter(
            this@MainActivity,
            android.R.layout.simple_dropdown_item_1line,
            gameModes
        )
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            main_btnPlay.id -> {
                startActivity(
                    Intent(this@MainActivity, OnPlay::class.java)
                        .putExtra("intent_gameMode", this.gameMode.toString())
                )
            }
            main_btnExit.id -> finishAffinity()
        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        this.gameMode = position
    }


    override fun onNothingSelected(parent: AdapterView<*>?) { }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.init();
    }
}
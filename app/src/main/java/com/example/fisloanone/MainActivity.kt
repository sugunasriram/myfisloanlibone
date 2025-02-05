package com.example.fisloanone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.myfisloanlibone.LoanLib

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLaunchLoan = findViewById<Button>(R.id.btnLaunchLoan)
        btnLaunchLoan.setOnClickListener {
            LoanLib.launchFirstScreen(this)

        }
    }
}
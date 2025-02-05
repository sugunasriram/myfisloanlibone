package com.example.myfisloanlibone

import android.content.Context
import android.content.Intent
import android.widget.Toast

object LoanLib {
    fun launchFirstScreen(context: Context) {
        Toast.makeText(context, "Launching First Screen", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, FirstScreenActivity::class.java)
        context.startActivity(intent)
    }
}
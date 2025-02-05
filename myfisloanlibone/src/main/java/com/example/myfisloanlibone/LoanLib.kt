package com.example.myfisloanlibone

import android.content.Context
import android.content.Intent

object LoanLib {
    fun launchFirstScreen(context: Context) {
        val intent = Intent(context, FirstScreenActivity::class.java)
        context.startActivity(intent)
    }
}
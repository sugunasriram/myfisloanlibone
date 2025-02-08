package com.example.fisloanone

//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import android.widget.Button
//import androidx.activity.ComponentActivity
//import androidx.compose.runtime.Composable
//import com.example.myfisloanlibone.LoanLib

//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val btnLaunchLoan = findViewById<Button>(R.id.btnLaunchLoan)
//        btnLaunchLoan.setOnClickListener {
//            LoanLib.launchFirstScreen(this)
//        }
//    }
//}


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.github.sugunasriram.myfisloanlibone.LoanLib



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            AndroidLibraryTheme {
//                Box(modifier = Modifier.fillMaxSize()) {
//
//                }
//            }
            LoanLib.ImagePreview()
        }
    }
}

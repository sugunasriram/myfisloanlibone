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
import java.io.Serializable
import com.github.sugunasriram.myfisloanlibone.LoanLib
import com.github.sugunasriram.myfisloanlibone.LoanLib.PersonalDetails
import com.github.sugunasriram.myfisloanlibone.LoanLib.ProductDetails


class MainActivity : ComponentActivity() {
    private fun PopulatePersonalDetails() : LoanLib.PersonalDetails {
        return PersonalDetails(
            name = "Suguna",
            dob = "01/01/1990",
            personalEmailId = "sugu@gmail.com",
            officialEmailId = "sugu@integra.com",
            gender = "female",
            address1 = "123 Main St",
            pincode1 = "560064",
            address2 = "456 Elm St",
            pincode2 = "560065"
        )

    }
    private fun PopulateProductDetails(): ProductDetails {
        return ProductDetails(
            productCategory = "Electronics",
            productSKUID = "SKU123",
            productBrand = "Samsung",
            productPrice = 10000.0,
            downpayment = 2000.0,
            merchantPan = "ABCDE1234F",
            merchantGst = "GST1234",
            merchantBankAccountNumber = "1234567890",
            merchantIfscCode = "IFSC1234",
            merchantBankAccountHolderName = "Pinnacle"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            AndroidLibraryTheme {
//                Box(modifier = Modifier.fillMaxSize()) {
//
//                }
//            }

            //2. Only Image
            //LoanLib.ImagePreview()

//        }
        //3. Image with 2 buttons - Working
        //LoanLib.DisplayImageAndTwoButtons(context = this)

        //LoanLib.LaunchThirdScreen(context = this)

        PopulatePersonalDetails()

        LoanLib.LaunchFISAppWithParams(
            context = this,
            personalDetails = PopulatePersonalDetails(),
            productDetails = PopulateProductDetails())
    }


}

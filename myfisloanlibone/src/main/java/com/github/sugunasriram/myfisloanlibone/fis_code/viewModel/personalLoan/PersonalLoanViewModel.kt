package com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.personalLoan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import com.github.sugunasriram.myfisloanlibone.R
import com.github.sugunasriram.myfisloanlibone.fis_code.navigation.navigateToLoanProcessScreen
import com.github.sugunasriram.myfisloanlibone.fis_code.viewModel.BaseViewModel

class PersonalLoanViewModel : BaseViewModel() {

    private val _adharNumberLink: MutableLiveData<Boolean> = MutableLiveData(false)
    val adharNumberLink: LiveData<Boolean> = _adharNumberLink

    fun onAdharNumberLinkChanged(adharNumberLink: Boolean) {
        _adharNumberLink.value = adharNumberLink
    }

    private val _consent: MutableLiveData<Boolean> = MutableLiveData(false)
    val consent: LiveData<Boolean> = _consent

    fun onConsentChanged(consent: Boolean) {
        _consent.value = consent
    }
    private val _generalError: MutableLiveData<String?> = MutableLiveData("")
    val generalError: LiveData<String?> = _generalError
    fun updateGeneralError(errorMsg: String?) {
        _generalError.value = errorMsg
    }

    fun clearMessage(newData: String? = null) {
        updateGeneralError(newData)
    }

    fun buttonValidation(
        adharNumberLink: Boolean,
        consent: Boolean,
        navController: NavHostController,
        context: Context,
        transactionId: String="Sugu",
        fromFlow: String
    ) {
        clearMessage()
        if (!adharNumberLink || !consent) {
            updateGeneralError(context.getString(R.string.provide_the_adhar_number_linked))
        } else {
            navigateToLoanProcessScreen(navController, transactionId,1, context.getString(R
                .string.loan), "1234",fromFlow = fromFlow)
        }
    }
}

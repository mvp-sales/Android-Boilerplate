package com.mvpsales.github.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvpsales.github.db.UsercentricsDataType
import com.mvpsales.github.db.UsercentricsDataTypeDao
import com.mvpsales.github.utils.Constants
import com.usercentrics.sdk.Usercentrics
import com.usercentrics.sdk.UsercentricsServiceConsent
import com.usercentrics.sdk.errors.UsercentricsError
import com.usercentrics.sdk.v2.settings.data.UsercentricsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

private val BANKING_SNOOPY_RULE_DATA_TYPES = listOf(
    Constants.PURCHASE_ACTIVITY, Constants.BANK_DETAILS, Constants.CREDIT_DEBIT_CARD_NUMBER
)

private val WHY_DO_YOU_CARE_RULE_DATA_TYPES = listOf(
    Constants.SEARCH_TERMS, Constants.GEOGRAPHIC_LOCATION, Constants.IP_ADDRESS
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataTypesDao: UsercentricsDataTypeDao
): ViewModel() {

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState

    fun onShowConsentBanner() {
        Usercentrics.isReady({ status ->
            if (status.shouldCollectConsent) {
                // Show banner to collect consent
                _uiState.postValue(UiState.ShowConsentBanner)
            } else {
                // Apply consent with status.consents
                applyConsents(status.consents)
            }
        }, { error ->
            // Handle non-localized error
            _uiState.postValue(UiState.UsercentricsSdkError(error))
        })
    }

    fun applyConsents(consents: List<UsercentricsServiceConsent>) {
        val consentedServicesIds = consents.map { consent -> consent.templateId }
        val cmpData = Usercentrics.instance.getCMPData()
        val services = cmpData.services.filter { consentedServicesIds.contains(it.templateId) }
        calculateConsentScore(services)
    }

    private fun calculateConsentScore(services: List<UsercentricsService>) {
        var totalConsentScore: Double = 0.0
        viewModelScope.launch(Dispatchers.IO) {
            services.forEach { service ->
                val dataTypes = dataTypesDao.getDataTypesFromConsent(service.dataCollectedList)
                val serviceCost = applyCostRules(dataTypes)
                println("${service.dataProcessor}: ${serviceCost.roundToInt()}")
                totalConsentScore += serviceCost
            }
            _uiState.postValue(UiState.ShowConsentScore(totalConsentScore))
        }
    }

    private fun applyCostRules(dataTypes: List<UsercentricsDataType>): Double {
        var serviceCost = dataTypes.sumOf { it.cost }.toDouble()
        val dataTypesCollected = dataTypes.map { it.dataTypeDescription }

        // Rule 1: Banking snoopy
        if (BANKING_SNOOPY_RULE_DATA_TYPES.all { it in dataTypesCollected }) {
            serviceCost *= Constants.BANKING_SNOOPY_RULE_COST_FACTOR
        }

        // Rule 2: Why do you care?
        if (WHY_DO_YOU_CARE_RULE_DATA_TYPES.all { it in dataTypesCollected }) {
            serviceCost *= Constants.WHY_DO_YOU_CARE_RULE_COST_FACTOR
        }

        // Rule 3: The good citizen
        if (dataTypes.count() <= Constants.GOOD_CITIZEN_RULE_NUM_MAX_DATA_TYPES) {
            serviceCost *= Constants.GOOD_CITIZEN_RULE_COST_FACTOR
        }

        return serviceCost
    }

    sealed class UiState {
        data object Initial: UiState()
        data object ShowConsentBanner: UiState()
        data class ShowConsentScore(val consentScore: Double): UiState()
        data class UsercentricsSdkError(val error: UsercentricsError): UiState()
    }
}
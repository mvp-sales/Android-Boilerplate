package com.mvpsales.github.ui

import android.util.Log
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

private const val USERCENTRICS_TAG = "USERCENTRICS_CHALLENGE"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataTypesDao: UsercentricsDataTypeDao
): ViewModel() {

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState.Initial)
    val uiState: LiveData<UiState> = _uiState

    fun onShowConsentBanner() {
        Usercentrics.isReady({ status ->
            if (status.shouldCollectConsent) {
                _uiState.postValue(UiState.ShowConsentBanner)
            } else {
                applyConsents(status.consents)
            }
        }, { error ->
            _uiState.postValue(UiState.UsercentricsSdkError(error))
        })
    }

    fun applyConsents(consents: List<UsercentricsServiceConsent>) {
        val consentedServicesIds = consents.filter { it.status }.map { consent -> consent.templateId }
        val cmpData = Usercentrics.instance.getCMPData()
        val services = cmpData.services.filter { consentedServicesIds.contains(it.templateId) }
        calculateConsentScore(services)
    }

    private fun calculateConsentScore(services: List<UsercentricsService>) {
        var totalConsentScore = 0.0
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(
                USERCENTRICS_TAG,
                """
                    ############################################
                    #                                          #
                    #       USERCENTRICS SERVICES START        #  
                    #                                          #
                    ############################################
                """.trimIndent()
            )
            services.forEach { service ->
                val dataTypes = dataTypesDao.getDataTypesFromConsent(service.dataCollectedList)
                val serviceCost = applyCostRules(dataTypes)
                Log.d(USERCENTRICS_TAG, "${service.dataProcessor}: ${serviceCost.roundToInt()}")
                totalConsentScore += serviceCost
            }
            Log.d(
                USERCENTRICS_TAG,
                """
                    ############################################
                    #                                          #
                    #       USERCENTRICS SERVICES END          #  
                    #                                          #
                    ############################################
                """.trimIndent()
            )
            _uiState.postValue(UiState.ShowConsentScore(totalConsentScore.roundToInt()))
        }
    }

    private fun applyCostRules(dataTypes: List<UsercentricsDataType>): Double {
        val baseServiceCost = dataTypes.sumOf { it.cost }.toDouble()
        val dataTypesCollected = dataTypes.map { it.dataTypeDescription }
        var totalServiceCost = baseServiceCost

        // Rule 1: Banking snoopy
        if (Constants.BANKING_SNOOPY_RULE_DATA_TYPES.all { it in dataTypesCollected }) {
            totalServiceCost += baseServiceCost * Constants.BANKING_SNOOPY_RULE_COST_FACTOR
        }

        // Rule 2: Why do you care?
        if (Constants.WHY_DO_YOU_CARE_RULE_DATA_TYPES.all { it in dataTypesCollected }) {
            totalServiceCost += baseServiceCost * Constants.WHY_DO_YOU_CARE_RULE_COST_FACTOR
        }

        // Rule 3: The good citizen
        if (dataTypes.count() <= Constants.GOOD_CITIZEN_RULE_NUM_MAX_DATA_TYPES) {
            totalServiceCost += baseServiceCost * Constants.GOOD_CITIZEN_RULE_COST_FACTOR
        }

        return totalServiceCost
    }

    fun onResetConsents() {
        Usercentrics.instance.clearUserSession({ _ ->
            _uiState.postValue(UiState.ConsentsReset)
        }, { error ->
            _uiState.postValue(UiState.UsercentricsSdkError(error))
        })
    }

    sealed class UiState {
        data object Initial: UiState()
        data object ShowConsentBanner: UiState()
        data class ShowConsentScore(val consentScore: Int): UiState()
        data class UsercentricsSdkError(val error: UsercentricsError): UiState()
        data object ConsentsReset: UiState()
    }
}
package com.mvpsales.github.utils

object Constants {
    private const val PURCHASE_ACTIVITY = "Purchase activity"
    private const val BANK_DETAILS = "Bank details"
    private const val CREDIT_DEBIT_CARD_NUMBER = "Credit and debit card number"
    private const val SEARCH_TERMS = "Search terms"
    private const val GEOGRAPHIC_LOCATION = "Geographic location"
    private const val IP_ADDRESS = "IP Address"
    const val GOOD_CITIZEN_RULE_NUM_MAX_DATA_TYPES = 4
    const val BANKING_SNOOPY_RULE_COST_FACTOR = 0.1
    const val WHY_DO_YOU_CARE_RULE_COST_FACTOR = 0.27
    const val GOOD_CITIZEN_RULE_COST_FACTOR = -0.1

    val BANKING_SNOOPY_RULE_DATA_TYPES = listOf(
        PURCHASE_ACTIVITY, BANK_DETAILS, CREDIT_DEBIT_CARD_NUMBER
    )
    val WHY_DO_YOU_CARE_RULE_DATA_TYPES = listOf(
        SEARCH_TERMS, GEOGRAPHIC_LOCATION, IP_ADDRESS
    )
}
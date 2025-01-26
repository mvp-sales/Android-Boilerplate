package com.mvpsales.github.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UsercentricsDataTypeDao {

    @Query("select * from usercentricsdatatype where dataTypeDescription in (:consentDataTypes)")
    suspend fun getDataTypesFromConsent(consentDataTypes: List<String>): List<UsercentricsDataType>
}
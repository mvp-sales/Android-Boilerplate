package com.mvpsales.github.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsercentricsDataTypeDao {

    @Query("select * from usercentricsdatatype")
    fun getAllDataTypes(): Flow<List<UsercentricsDataType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dataTypes: List<UsercentricsDataType>)

    @Query("select * from usercentricsdatatype where dataTypeDescription in (:consentDataTypes)")
    fun getDataTypesFromConsent(consentDataTypes: List<String>): List<UsercentricsDataType>
}
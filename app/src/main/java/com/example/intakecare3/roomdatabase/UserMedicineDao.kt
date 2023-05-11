package com.example.intakecare3.roomdatabase

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.*
import io.realm.mongodb.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserMedicineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUserMedicine(userMedicine: UserMedicine)

    @Query("SELECT * FROM Medicine_for_users_table ORDER BY medicineName ASC")
    fun readAllData(): LiveData<List<UserMedicine>>

    @Query("SELECT * FROM Medicine_for_users_table WHERE prodCode LIKE :prodCode LIMIT 1")
    suspend fun findDosageDetails(prodCode: String): UserMedicine

    @Query("UPDATE Medicine_for_users_table SET medicineTotalNbDosage=:medicineTotalNbDosage WHERE prodCode=:prodCode")
    fun updatePillNumber(prodCode: String, medicineTotalNbDosage: Int) : Int

  /* @Query("SELECT prodCode FROM medications_table WHERE prodCode = :prodCode LIMIT 1")
     suspend fun informationExists(prodCode : String?): UserMedicine?*/

  /* @Query("SELECT * FROM medications_table WHERE prodCode = :prodCode")
     suspend fun readProdCode()
      @Delete
    fun deleteUserMedicine(userMedicine: UserMedicine)*/


}




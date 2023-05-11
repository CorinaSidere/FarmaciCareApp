package com.example.intakecare3.roomdatabase


import androidx.lifecycle.LiveData

class UserMedicineRepository(private val userMedicineDao: UserMedicineDao) {

    val readAllData: LiveData<List<UserMedicine>> = userMedicineDao.readAllData()


     suspend fun addUserMedicine(userMedicine: UserMedicine){
        userMedicineDao.addUserMedicine(userMedicine)
    }


}
package com.example.intakecare3.roomdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.realm.mongodb.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<UserMedicine>>
    private val repository: UserMedicineRepository


    init {
        val userMedicineDao = UserMedicineDatabase.getDatabase(application).userMedicineDao()
        repository = UserMedicineRepository(userMedicineDao)
        readAllData = repository.readAllData
    }
    //Kotlin co-routines; dispatcher.io means the code runs in a background thread, not the main thread
    fun addUserMedicine(userMedicine: UserMedicine){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUserMedicine(userMedicine)
        }
    }


}
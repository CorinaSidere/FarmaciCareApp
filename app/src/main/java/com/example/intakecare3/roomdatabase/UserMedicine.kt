package com.example.intakecare3.roomdatabase

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Medicine_for_users_table")
data class UserMedicine (
    val id: Int,
    var medicineName: String,
    var medicineDosage : String,
    var medicineNbDosage : Int,
    var medicineTotalNbDosage: Int,
    @PrimaryKey(autoGenerate = false)
    var prodCode : String,
    var expDate : String,
    var ltNumber : String,
    var serNumber : String
        ):java.io.Serializable
//define key constant that can be used to transport the values from one activity to another through .putExtra()
const val ARG_MED = "argMed"

package com.example.intakecare3.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserMedicine::class], version = 1, exportSchema = false)

abstract class UserMedicineDatabase: RoomDatabase() {

    abstract fun userMedicineDao() : UserMedicineDao

    companion object{
        @Volatile
        private var INSTANCE : UserMedicineDatabase? = null

        fun getDatabase(context: Context): UserMedicineDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserMedicineDatabase::class.java,
                    "Medicine_for_users_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
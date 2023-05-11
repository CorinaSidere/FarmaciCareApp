package com.example.intakecare3.changepasswordphase

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class UpdatePassRequest (
     private val accessToken : String,
     private val oldPwd : String,
     private val newPwd: String,
        )
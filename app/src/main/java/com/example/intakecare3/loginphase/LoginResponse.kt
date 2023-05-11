package com.example.authentication3

import com.example.intakecare3.loginphase.UserDetails
import com.google.gson.annotations.SerializedName

data class LoginResponse(
     @SerializedName("user"         ) var user        : UserDetails?   = null,
     @SerializedName("access_token" ) var accessToken : String? = null

)

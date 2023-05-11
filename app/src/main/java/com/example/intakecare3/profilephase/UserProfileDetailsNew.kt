package com.example.intakecare3.profilephase

import com.google.gson.annotations.SerializedName

public data class UserProfileDetailsNew (

    @SerializedName("email"     ) var email    : String?  = null,
    @SerializedName("cf"        ) var cf       : String?  = null,
    @SerializedName("user_type" ) var userType : String?  = null,
    @SerializedName("phone"     ) var phone    : String?  = null
        )
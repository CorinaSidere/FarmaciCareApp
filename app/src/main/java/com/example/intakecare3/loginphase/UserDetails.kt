package com.example.intakecare3.loginphase

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

//user details class used in the login of all users; returns all their info
//// must be used to automatically take this info and put it in the profile section

public final data class UserDetails(
   @SerializedName("_id"       ) var Id       : String?  = null,
   @SerializedName("new_user"  ) var newUser  : Boolean? = null,
   @SerializedName("email"     ) var email    : String?  = null,
   @SerializedName("cf"        ) var cf       : String?  = null,
   @SerializedName("user_type" ) var userType : String?  = null,
   @SerializedName("__v"       ) var _v       : Int?     = null,
   @SerializedName("name"      ) var name     : String?  = null,
   @SerializedName("phone"     ) var phone    : String?  = null,
   @SerializedName("surname"   ) var surname  : String?  = null,
   @SerializedName("username"  ) var username : String?  = null
)
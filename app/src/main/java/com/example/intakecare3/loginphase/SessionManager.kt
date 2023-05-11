package com.example.intakecare3.loginphase

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.connectingtomdb.R
import com.example.intakecare3.pharmacyphase.PharmacyIntakesDetails
import com.example.intakecare3.profilephase.UserProfileDetailsNew

class SessionManager(context: Context) {

    private var prefs : SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object{
        const val USER_TOKEN = "user_token"
        const val KEY_CF = "user_cf"
        const val KEY_NAME = "user_name"
        const val KEY_EMAIL = "user_email"
        const val KEY_PHONE = "user_phone"
        const val KEY_USERTYPE = "user_type"
        const val KEY_USERID = "user_id"
        const val KEY_USERNAME = "username"
        const val KEY_PHARMDATE = "programmed_date"
        const val KEY_PHARMSTATUS = "intake_status"
    }

    //function to save the auth token

    fun saveAuthToken(token2:String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token2)
        editor.apply()
        //Log.d("IMPORTANT", "Auth token is: $token2")
    }

    //function to fetch the auth token

    fun fetchAuthToken():String? {
        return prefs.getString(USER_TOKEN,null)
    }

    //clear token
    fun clear(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    //function to save user details
    fun saveUserDetails(userDetails2: UserDetails){
        val editor = prefs.edit()
        editor.putString(KEY_CF,userDetails2.cf)
        editor.putString(KEY_NAME,userDetails2.username)
        editor.putString(KEY_EMAIL,userDetails2.email)
        editor.putString(KEY_PHONE,userDetails2.phone)
        editor.putString(KEY_USERTYPE,userDetails2.userType)
        editor.putString(KEY_USERNAME,userDetails2.username)
        editor.putString(KEY_USERID,userDetails2.Id)
        editor.apply()

        Log.d("IMPORTANT", "The credentials are: $userDetails2")
    }
    //function to save profile user details modified by user
    fun saveProfileUserDetails(userDetails4:UserProfileDetailsNew){
        val editor = prefs.edit()
        editor.putString(KEY_CF,userDetails4.cf)
        editor.putString(KEY_EMAIL,userDetails4.email)
        editor.putString(KEY_PHONE,userDetails4.phone)
        editor.putString(KEY_USERTYPE,userDetails4.userType)
        editor.apply()
        Log.d("IMPORTANT", "The new user details are: $userDetails4")
    }

    //function to fetch user details
    fun fetchUserCF():String?{
        return prefs.getString(KEY_CF,null)
    }
    fun fetchUserName():String?{
        return prefs.getString(KEY_NAME,null)
    }
    fun fetchUserEmail():String?{
        return prefs.getString(KEY_EMAIL,null)
    }
    fun fetchUserPhone():String?{
        return prefs.getString(KEY_PHONE,null)
    }
    fun fetchUserType():String?{
        return prefs.getString(KEY_USERTYPE,null)
    }

    //function to fetch the user id and username
    fun fetchUserId():String?{
        return prefs.getString(KEY_USERID,null)
    }
    fun fetchUsername():String?{
        return prefs.getString(KEY_USERNAME,null)
    }


}
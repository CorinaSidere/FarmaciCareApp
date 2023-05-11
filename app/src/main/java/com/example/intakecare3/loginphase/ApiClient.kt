package com.example.authentication3

import android.content.Context
import com.example.intakecare3.changepasswordphase.ChangePwService
import com.example.intakecare3.loginphase.AuthInterceptor
import com.example.intakecare3.loginphase.SessionManager
import com.example.intakecare3.loginphase.UserService
import com.example.intakecare3.pharmacyphase.PharmacyService
import com.example.intakecare3.profilephase.EditProfileService
import com.example.intakecare3.therapiesphase.TherapyService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


public class ApiClient {

    private fun getRetrofit(context: Context) : Retrofit{
             val sessionManager = SessionManager(context)

        val httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor) //one app interceptor for login
            .addInterceptor(AuthInterceptor(context)) //one interceptor for remembering the access token
            .build()

        val retrofit : Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://intake-care.herokuapp.com/")
            .client(okHttpClient)
            .build()
        return retrofit
    }

    public fun getUserService(context: Context) : UserService {
        val userService: UserService = getRetrofit(context).create(UserService::class.java)
        return userService
    }

    public fun getTherapyService(context: Context) : TherapyService {
        val therapyService : TherapyService = getRetrofit(context).create(TherapyService::class.java)
        return therapyService
    }

    public fun getNewPasswordService(context: Context): ChangePwService{
        val changePassword : ChangePwService = getRetrofit(context).create(ChangePwService::class.java)
        return changePassword
    }

    public fun getEditProfileService(context: Context): EditProfileService{
        val editProfileService: EditProfileService = getRetrofit(context).create(EditProfileService::class.java)
        return editProfileService
    }
    public fun getPharmacyIntakesService(context: Context) : PharmacyService{
        val pharmacyIntakesService : PharmacyService = getRetrofit(context).create(PharmacyService::class.java)
        return pharmacyIntakesService
    }


}
package com.example.intakecare3.loginphase

import com.example.authentication3.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


public interface UserService {
    // POST login credentials for authentication
    @POST("auth/login/")
    fun userLogin(@Body loginRequest : LoginRequest): Call<LoginResponse>

}
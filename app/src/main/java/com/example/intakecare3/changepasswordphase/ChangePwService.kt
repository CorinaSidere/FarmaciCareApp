package com.example.intakecare3.changepasswordphase

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST

public interface ChangePwService {

    @PATCH("auth/change/")
    fun changePassword(@Body updatePassRequest: UpdatePassRequest): Call<UpdatePassResponse>
}
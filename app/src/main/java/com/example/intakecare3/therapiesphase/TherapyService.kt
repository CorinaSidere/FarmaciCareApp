package com.example.intakecare3.therapiesphase

import retrofit2.Call
import retrofit2.http.GET

public interface TherapyService {

    //GET therapies for patient
      @GET("patients/therapies/")
      fun  getTherapy(): Call<List<TherapyDetails>>
}
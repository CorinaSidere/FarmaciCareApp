package com.example.intakecare3.pharmacyphase

import com.example.intakecare3.therapiesphase.TherapyDetails
import retrofit2.Call
import retrofit2.http.*

public interface PharmacyService {

    //GET intakes for patient
    @GET("intakes/")
    fun getIntakes(): Call<List<PharmacyIntakesDetails>> //for specific therapy: use therapy_id in the body of this?;
}
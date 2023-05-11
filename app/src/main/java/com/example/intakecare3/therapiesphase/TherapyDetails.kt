package com.example.intakecare3.therapiesphase

import com.example.intakecare3.therapiesphase.details.Delivery
import com.google.gson.annotations.SerializedName


data class TherapyDetails (

    @SerializedName("_id"        ) var Id         : String?   = null,
    @SerializedName("patient_id" ) var patientId  : String?   = null,
    @SerializedName("drug"       ) var drug       : String?   = null,
    @SerializedName("delivery"   ) var delivery   : Delivery? = Delivery(),
    @SerializedName("start_date" ) var startDate  : String?   = null,
    @SerializedName("meals"      ) var meals      : String?   = null,
    @SerializedName("posology"   ) var posology   : String?   = null,
    @SerializedName("state"      ) var state      : Boolean?  = null,
    @SerializedName("edit"       ) var edit       : String?   = null,
    @SerializedName("validation" ) var validation : String?   = null,
    @SerializedName("adherence"  ) var adherence  : Int?      = null,
    @SerializedName("__v"        ) var _v         : Int?      = null

): java.io.Serializable

//const val THERAPY_KEY = "therapy_key"
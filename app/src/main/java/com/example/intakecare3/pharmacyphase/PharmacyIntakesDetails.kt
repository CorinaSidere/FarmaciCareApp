package com.example.intakecare3.pharmacyphase

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

data class PharmacyIntakesDetails (
        @SerializedName("_id"             ) var Id             : String? = null,
        @SerializedName("programmed_date" ) var programmedDate : String? = null,
        @SerializedName("max_delay"       ) var maxDelay       : Int?    = null,
        @SerializedName("status"          ) var status         : String? = null,
        @SerializedName("therapy_id"      ) var therapyId      : String? = null,
        @SerializedName("__v"             ) var _v             : Int?    = null,
        @SerializedName("drug"            ) var drug           : String? = null,
        @SerializedName("posology"        ) var posology       : String? = null
        )
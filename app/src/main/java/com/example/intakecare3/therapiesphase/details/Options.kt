package com.example.intakecare3.therapiesphase.details

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Options (

  @SerializedName("cadence"   ) var cadence  : Int?    = null,
  @SerializedName("max_delay" ) var maxDelay : Int?    = null,
  @SerializedName("time"      ) var time     : String? = null

):Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readValue(Int::class.java.classLoader) as? Int,
    parcel.readString()
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(cadence)
    parcel.writeValue(maxDelay)
    parcel.writeString(time)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Options> {
    override fun createFromParcel(parcel: Parcel): Options {
      return Options(parcel)
    }

    override fun newArray(size: Int): Array<Options?> {
      return arrayOfNulls(size)
    }
  }
}
package com.example.intakecare3.therapiesphase.details

import android.os.Parcel
import android.os.Parcelable
import com.example.intakecare3.therapiesphase.details.Options
import com.google.gson.annotations.SerializedName


data class Delivery (

  @SerializedName("scheduling_type" ) var schedulingType : String?            = null,
  @SerializedName("options"         ) var options        : ArrayList<Options> = arrayListOf()

):Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readString(),
    TODO("options")
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(schedulingType)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Delivery> {
    override fun createFromParcel(parcel: Parcel): Delivery {
      return Delivery(parcel)
    }

    override fun newArray(size: Int): Array<Delivery?> {
      return arrayOfNulls(size)
    }
  }
}
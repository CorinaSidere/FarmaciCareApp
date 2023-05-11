package com.example.intakecare3.profilephase

import com.example.intakecare3.loginphase.UserDetails
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH

public interface EditProfileService {

    //PATCH the UserDetails with UserProfileDetailsNew; does not work yet bc there is no option for this in the BE
    @PATCH("patients/")
    fun updateUserDetails(@Body userDetails4: UserProfileDetailsNew): Call<UserDetails>
}
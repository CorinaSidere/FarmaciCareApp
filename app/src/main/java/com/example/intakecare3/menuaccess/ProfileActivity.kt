package com.example.intakecare3.menuaccess

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.authentication3.ApiClient
import com.example.intakecare3.loginphase.SessionManager
import com.example.connectingtomdb.R
import com.example.intakecare3.GUIActivity
import com.example.intakecare3.changepasswordphase.UpdatePassRequest
import com.example.intakecare3.changepasswordphase.UpdatePassResponse
import com.example.intakecare3.loginphase.UserDetails
import com.example.intakecare3.profilephase.UserProfileDetailsNew
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var userDetails: UserDetails
    private lateinit var apiClient: ApiClient

    private lateinit var codiceFiscale : TextView
    private lateinit var nameOfUser : TextView
    private lateinit var email : TextView
    private lateinit var phoneNumber : TextView
    private lateinit var userType : TextView
    private lateinit var editBtn : Button
    private lateinit var saveBtn : Button

    private lateinit var oldPassW: EditText
    private lateinit var newPassW: EditText
    private lateinit var confirmNewPw: EditText
    private lateinit var confirmBtn : Button

    private lateinit var userId: String
    private lateinit var userNameId : String
    private lateinit var confirmedPW : String
    private lateinit var userDetails4 : UserProfileDetailsNew

    private lateinit var btnPrevious : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)
        userDetails = UserDetails()
        apiClient = ApiClient()
        userDetails4 = UserProfileDetailsNew()


        codiceFiscale = findViewById(R.id.cfAnswer)
        nameOfUser = findViewById(R.id.newName)
        email = findViewById(R.id.emailAnswer)
        phoneNumber = findViewById(R.id.phoneAnswer)
        userType = findViewById(R.id.usertypeAnswer)
        editBtn = findViewById(R.id.btnEdit)
        saveBtn = findViewById(R.id.btnSave)

        //saved in shared preferences and displayed in profile
        codiceFiscale.text = sessionManager.fetchUserCF().toString()
        nameOfUser.text =  sessionManager.fetchUserName().toString()
        email.text =  sessionManager.fetchUserEmail().toString()
        phoneNumber.text =  sessionManager.fetchUserPhone().toString()
        userType.text =  sessionManager.fetchUserType().toString()

        saveBtn.visibility = View.INVISIBLE
        editBtn.setOnClickListener(View.OnClickListener{
            //make a fragment with all these fields as EditTexts and possibility to modify;
            //then add SAVE btn and make all modify also in here + save in session manager!
            //USE FRAGMENT NAVIGATION!!!
            saveBtn.visibility = View.VISIBLE
            editBtn.visibility = View.INVISIBLE
        })

            saveBtn.setOnClickListener(View.OnClickListener {
                val newCodiceFiscale = codiceFiscale.text.toString()
                val newEmail = email.text.toString()
                val newPhoneNumber = phoneNumber.text.toString()
                val newUserType = userType.text.toString()

                userDetails4.cf = newCodiceFiscale
                userDetails4.email = newEmail
                userDetails4.phone = newPhoneNumber
                userDetails4.userType = newUserType

                sessionManager.saveProfileUserDetails(userDetails4)
                editBtn.visibility = View.VISIBLE
                saveBtn.visibility = View.INVISIBLE

                //UPDATE user details to the server
                apiClient.getEditProfileService(this).updateUserDetails(userDetails4).enqueue(object: Callback<UserDetails>{
                    override fun onResponse(
                        call: Call<UserDetails>,
                        response: Response<UserDetails>
                    ) {
                        val userResponse: UserDetails? = response.body()
                        if (response.isSuccessful){
                            Toast.makeText(this@ProfileActivity,"Profile successfully updated with $userResponse.", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@ProfileActivity,"Profile failed to update.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UserDetails>, t: Throwable) {
                        Toast.makeText(this@ProfileActivity,t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                })

            })

        //EDIT BTN onclicklistener for editing the text fields, plus save btn to save the info modified

        oldPassW = findViewById(R.id.oldPwAnswer)
        newPassW = findViewById(R.id.newPwAnswer)
        confirmNewPw = findViewById(R.id.RnewPwAnswer)
        confirmBtn = findViewById(R.id.btnConfirmNewPw)
        //fetch id and username from session manager
        userId = sessionManager.fetchUserId().toString()
        userNameId = sessionManager.fetchUsername().toString()



        confirmBtn.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(oldPassW.text.toString()) || TextUtils.isEmpty(newPassW.text.toString()) || TextUtils.isEmpty(confirmNewPw.text.toString())) {
                Toast.makeText(
                    this,
                    "Must input credentials.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //proceed to changing pw
                confirmedPW = confirmNewPw.toString()
                changePassword()

            }
        })

        //buttons
        btnPrevious = findViewById(R.id.btnPrevious)

        btnPrevious.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,GUIActivity::class.java))
        })
    }

    private fun changePassword() {
        var userOldPass : String = oldPassW.text.toString().trim()
        var userNewPass : String = newPassW.text.toString().trim()
        var userConfirmPass : String = confirmNewPw.text.toString().trim()
        var accessToken : String = sessionManager.fetchAuthToken().toString()

        if (userOldPass.isEmpty()){
            Toast.makeText(this, "Enter current password.", Toast.LENGTH_LONG).show()
        }
        if (userNewPass.isEmpty()){
            Toast.makeText(this, "Enter new password.", Toast.LENGTH_LONG).show()
        }
        if (userConfirmPass.isEmpty()){
            Toast.makeText(this, "Enter password confirmation.", Toast.LENGTH_LONG).show()
        }

        if (userNewPass == userConfirmPass){
            confirmedPW = userNewPass
        }else{
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
        }
        //PASSWORD IS HASHED? -> NO
        val updatePassRequest = UpdatePassRequest(accessToken,userOldPass,confirmedPW)
        apiClient.getNewPasswordService(this).changePassword(updatePassRequest).enqueue(object: Callback<UpdatePassResponse>{
            override fun onResponse(
                call: Call<UpdatePassResponse>,
                response: Response<UpdatePassResponse>
            ) {
                val passwordResponse: UpdatePassResponse? = response.body()
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Change successful!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Change failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdatePassResponse>, t: Throwable) {
                Toast.makeText(this@ProfileActivity,t.localizedMessage,Toast.LENGTH_SHORT).show()
                 }

        })
    }


}


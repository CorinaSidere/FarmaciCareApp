package com.example.intakecare3.loginphase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication3.ApiClient
import com.example.authentication3.LoginResponse
import com.example.connectingtomdb.R
import com.example.intakecare3.GUIActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LogInActivity : AppCompatActivity() {


    private lateinit var apiClient: ApiClient
    private lateinit var sessionManager: SessionManager

    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var btnLog: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        username = findViewById(R.id.etUsername)
        password = findViewById(R.id.etPassword)
        btnLog = findViewById(R.id.loginbtn)


        btnLog.setOnClickListener(View.OnClickListener() {
            if (TextUtils.isEmpty(username.text.toString()) || TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(
                    this,
                    "Must input username and password.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //proceed to login
                login()

            }
        })

        //verify if token already exists so that login is not necessary again
        if (sessionManager.fetchAuthToken() != null){
            startActivity(Intent(this, GUIActivity::class.java))
        }

        //hash password block
        /* val hashPass : String = BCrypt.withDefaults().hashToString(12, password.toCharArray())
                Log.d("register", "$hashPass")*/


    }
    fun login(){

        val loginRequest = LoginRequest(username.text.toString(), password.text.toString())

        apiClient.getUserService(this).userLogin(loginRequest).enqueue(object:
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                if (response.isSuccessful){
                    Toast.makeText(this@LogInActivity, "Login Successful.", Toast.LENGTH_LONG).show()
                    val loginResponse: LoginResponse? = response.body()

                    var userDetails : Response<LoginResponse> = response

                    Handler().postDelayed({
                        if (loginResponse != null) {
                            startActivity(
                                Intent(
                                    this@LogInActivity,
                                    GUIActivity::class.java
                                ).putExtra("data", loginResponse.accessToken)
                            )
                            loginResponse.accessToken?.let { sessionManager.saveAuthToken(it) }
                            //save user details
                            loginResponse.user.let {
                                if (it != null) {
                                    sessionManager.saveUserDetails(it)
                                }
                            }
                            Log.d("TAG", "Token saved.")
                        }
                    }, 700)

                }else{
                    Toast.makeText(this@LogInActivity, "Login Failed.", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LogInActivity, "Throwable "+ t.localizedMessage, Toast.LENGTH_LONG).show()

            }


        } )

    }
    //function that exits the app on back button pressed after logout
   override fun onBackPressed() {
        sessionManager.clear()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
        super.onBackPressed()
    }


}










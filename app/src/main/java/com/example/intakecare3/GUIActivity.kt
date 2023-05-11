package com.example.intakecare3

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.work.*
import com.example.connectingtomdb.R
import com.example.intakecare3.loginphase.LogInActivity
import com.example.intakecare3.loginphase.SessionManager
import com.example.intakecare3.menuaccess.*

class GUIActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    //for Medicine levels calculation
    //private lateinit var pharmacyIntakesDetails: PharmacyIntakesDetails
    //private lateinit var pharmacyIntakesDetailsStatus : String
   // private lateinit var pharmacyIntakesDetailsProgrammedDate : LocalDate
  //  private var userMedicineTotal : Int = 0
    //private var userMedicineCurrent : Int = 0
    //private var newDosage : Int = 0


    private lateinit var qrScan : CardView
    private lateinit var theRapy : CardView
    private lateinit var proFile : CardView
    private lateinit var pharMacy : CardView
    private lateinit var conNect : CardView
    private lateinit var setTings : ImageView
    private lateinit var logOut : ImageView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guiactivity)
        sessionManager = SessionManager(this)

         //bring user medicine values here

        //start the service for medicine calculation and notifications
       // startService(Intent(this,MyService::class.java).putExtra(ARG_MED,valuesFromUserMedicine))

        /*pharmacyIntakesDetails = PharmacyIntakesDetails()
        //obtain the right variables from the pre-initialized ones for Medicine level calculation
        pharmacyIntakesDetailsStatus = pharmacyIntakesDetails.status.toString()
        pharmacyIntakesDetailsProgrammedDate = pharmacyIntakesDetails.programmedDate!!
        userMedicineTotal = valuesFromUserMedicine.medicineTotalNbDosage
        userMedicineCurrent = valuesFromUserMedicine.medicineNbDosage*/


        //make buttons for each section and direct them properly
        qrScan = findViewById(R.id.btnqrcodescan)
        theRapy = findViewById(R.id.btntherapy)
        proFile = findViewById(R.id.btnprofile)
        pharMacy = findViewById(R.id.btnpharmacy)
        conNect = findViewById(R.id.btnconnectalexa)
        setTings = findViewById(R.id.btnsettings)
        logOut = findViewById(R.id.btnLogout)

        //start the QR scanner
        qrScan.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, QRScannerActivity::class.java))
        })

        //start the therapy section (recycle view)
        theRapy.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, TherapyLogsActivity::class.java))
        })

        //start the profile section
        proFile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        })

        //start the pharmacy section
        pharMacy.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PharmacyListDisplayActivity::class.java))
        })

        //start the connect to alexa section
      /*  conNect.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, AlexaActivity::class.java))
        })

        //start the settings section
        setTings.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        })*/

        //log out section
        logOut.setOnClickListener(View.OnClickListener {
            sessionManager.clear()
            //logout for sure!
            val intent : Intent = Intent(this, LogInActivity::class.java)
           // intent.putExtra("finish", true); if we check for  this in other activities, but no
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
           // stopService(Intent(this,MyService::class.java))
        })



     /*   //Medicine levels calculation function + notifications
        val currentDate : LocalDate = LocalDate.now()
        if (currentDate.isEqual(pharmacyIntakesDetailsProgrammedDate)) {
            calculateMedicineLevel()
        }*/
    }
    //on back pressed so that when exiting the app it won't retain any history; a bit of an oof when exiting but works fine enough
    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

 /*    fun periodicTimeWork(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myWorkRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,1, TimeUnit.HOURS
        ).setConstraints(constraints)
            .addTag("Medicine levels calculation")
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Medicine levels calculation", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest)
    }

    fun calculateMedicineLevel(){
        if (pharmacyIntakesDetailsStatus == "saved"){
            newDosage = userMedicineTotal - userMedicineCurrent
            Log.d("NOTIFICATION RESULT", "New dosage is : $newDosage")
        }else if (pharmacyIntakesDetailsStatus == "missed"){
            newDosage = userMedicineTotal
            Log.d("NOTIFICATION RESULT", "No new dosage!")
        }
        periodicTimeWork()
        }*/
    }

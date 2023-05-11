package com.example.intakecare3.servicemodule

/*import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock.elapsedRealtime
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.intakecare3.loginphase.SessionManager
import com.example.intakecare3.pharmacyphase.PharmacyIntakesDetails
import com.example.intakecare3.workmanager.MyWorker
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

        //MOVE SERVICE START TO GUI OR SMTH!
class MyService : Service() {

    private lateinit var myService: MyService
    //for Medicine levels calculation
    private lateinit var pharmacyIntakesDetailsStatus : String
    private lateinit var pharmacyIntakesDetailsProgrammedDate : String
    private lateinit var newPharmacyIntakeDate : LocalDate
    private var userMedicineTotal : Int = 0
    private var userMedicineCurrent : Int = 0
    private var newDosage : Int = 0
    private lateinit var currentDate : LocalDate
    private lateinit var sessionManager: SessionManager


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        sessionManager = SessionManager(this)
        //set user medicine values
        pharmacyIntakesDetailsProgrammedDate = sessionManager.fetchProgrammedDate().toString()
        println(pharmacyIntakesDetailsProgrammedDate)
        pharmacyIntakesDetailsStatus = sessionManager.fetchIntakesStatus().toString()
        println(pharmacyIntakesDetailsStatus)
        //get extra values from the other activity
        val extras = intent.extras
        if (extras != null) {
            userMedicineCurrent = extras.getInt("medicineNbDosage")
            userMedicineTotal = extras.getInt("medicineTotalNbDosage")
            println(userMedicineCurrent)
            println(userMedicineTotal)

        }

        //Medicine levels calculation function + notifications;
        currentDate = LocalDate.now()
        println(currentDate)
        dateTimeFormatter()
        //calculate medicine intake
        Timer().scheduleAtFixedRate(timerTask {
        if (currentDate.isEqual(newPharmacyIntakeDate)) {
            calculateMedicineLevel()
        }else {
            Log.d("IMPORTANT", "Therapy dates do not align!")
        }
        }, 5000,5000)


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        myService = MyService()
       myService.stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
       return null
    }
    //NOT WORKING, but maybe try other solutions?
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, this.javaClass)
        val restartServicePendingIntent = PendingIntent.getService(applicationContext, 1, restartServiceIntent, PendingIntent.FLAG_IMMUTABLE)
        val alarmService = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService[AlarmManager.ELAPSED_REALTIME, elapsedRealtime() + 500] =
            restartServicePendingIntent
        Log.d("taskremoved", "task removed ")
        super.onTaskRemoved(rootIntent)
    }

   private fun periodicTimeWork(){
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
  private  fun calculateMedicineLevel(){
        if (pharmacyIntakesDetailsStatus == "saved"){
            newDosage = userMedicineTotal - userMedicineCurrent
            Log.d("NOTIFICATION RESULT", "New dosage is : $newDosage")
            //when the status is finally "saved", then the calculation can be made;
            // and the calculation continues in a loop, and so we also need the periodicTimeWork()
            // function so it will show notification when the medicine level is below 30%
        }else if (pharmacyIntakesDetailsStatus == "missed"){
            newDosage = userMedicineTotal
            Log.d("NOTIFICATION RESULT", "No new dosage!")
        }
        if(newDosage <= 0.3*userMedicineTotal){
              periodicTimeWork()
        }else{
          Log.d("NOTIFICATION RESULT", "Dosage is not yet below 30%.")
      }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTimeFormatter(){
      val receivedDate = pharmacyIntakesDetailsProgrammedDate.substring(0,10)
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        newPharmacyIntakeDate = LocalDate.parse(receivedDate,formatter)
        println(newPharmacyIntakeDate)

    }
}*/
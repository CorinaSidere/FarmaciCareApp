package com.example.intakecare3.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.authentication3.ApiClient
import com.example.connectingtomdb.R
import com.example.intakecare3.GUIActivity
import com.example.intakecare3.pharmacyphase.PharmacyIntakesDetails
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
/*@HiltWorker
public class MyWorker @AssistedInject constructor(@Assisted context: Context, @Assisted workerParameters: WorkerParameters): Worker(context, workerParameters) {

    private lateinit var pharmacyIntakesDetailsStatus : String
    private lateinit var pharmacyIntakesDetailsProgrammedDate : String
    private lateinit var newPharmacyIntakeDate : LocalDate
    private var userMedicineTotal : Int = 0
    private var userMedicineCurrent : Int = 0
    private var newDosage : Int = 0
    private lateinit var currentDate : LocalDate
    //private lateinit var apiClient : ApiClient


    companion object{
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
       // apiClient = ApiClient()

        //get pharmacy intakes details
      //  getPharmacyIntakesDetails()
        pharmacyIntakesDetailsProgrammedDate = inputData.getString("programmedDate")!!
        pharmacyIntakesDetailsStatus = inputData.getString("status")!!


        val medicineValues1 = inputData.getString("medicineNbDosage")!!.toInt()
        val medicineValues2 = inputData.getString("medicineTotalNbDosage")!!.toInt()

            userMedicineCurrent = medicineValues1
            userMedicineTotal = medicineValues2
            println(userMedicineCurrent)
            println(userMedicineTotal)


        //Medicine levels calculation function + notifications;
        currentDate = LocalDate.now()
        println(currentDate)
        dateTimeFormatter()
        //calculate medicine intake
            if (currentDate.isEqual(newPharmacyIntakeDate)) {
                calculateMedicineLevel()
            }else {
                Log.d("IMPORTANT", "Therapy dates do not align!")
            }

        Log.d("doWork", "Success launching notification.")
        belowThirtyPercentNotification()
        return Result.success()
    }


    //function to make notification
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(){
        val intent = Intent(applicationContext, GUIActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_IMMUTABLE)

        val notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_round)
            .setContentTitle("Medication Warning!")
            .setContentText("Medication level is below 30%. Make sure to refill!")
            .setPriority(Notification.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channelName = "InTakeCare Notifications"
            val channelDescription = "InTakeCare Official Notifications channel"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName,channelImportance).apply {
                description = channelDescription
            }
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)){
            notify(NOTIFICATION, notification.build())
        }
    }

   /* private fun getPharmacyIntakesDetails() {
        //GET intakes for patient; we select the STATUS for running in the background ->
        // if it's "taken" -> take the medicineNbDosage from PharmacyListActivity and take away 1
        apiClient.getPharmacyIntakesService(applicationContext).getIntakes().enqueue(object:
            Callback<List<PharmacyIntakesDetails>> {
            override fun onResponse(
                call: Call<List<PharmacyIntakesDetails>>,
                response: Response<List<PharmacyIntakesDetails>>
            ) {
                if (response.isSuccessful){
                    val pharmacyIntakesResponse : List<PharmacyIntakesDetails> = response.body()!!
                    pharmacyIntakesDetailsProgrammedDate = pharmacyIntakesResponse.get(1).programmedDate.toString()
                    println(pharmacyIntakesDetailsProgrammedDate)
                    pharmacyIntakesDetailsStatus = pharmacyIntakesResponse.get(3).status.toString()
                    println(pharmacyIntakesDetailsStatus)

                }
                Log.d("SUCCESS", response.body().toString())

            }
            override fun onFailure(call: Call<List<PharmacyIntakesDetails>>, t: Throwable) {
                Log.e("FAILURE", t.localizedMessage)
            }

        })
    }*/

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateTimeFormatter(){
        val receivedDate = pharmacyIntakesDetailsProgrammedDate.substring(0,10)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        newPharmacyIntakeDate = LocalDate.parse(receivedDate,formatter)
        println(newPharmacyIntakeDate)

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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun belowThirtyPercentNotification(){
        if(newDosage <= 0.3*userMedicineTotal){
            showNotification()
        }else{
            Log.d("NOTIFICATION RESULT", "Dosage is not yet below 30%.")
        }
    }

        }*/
package com.example.intakecare3.pharmacyphase

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.connectingtomdb.R
import com.example.intakecare3.menuaccess.PharmacyListDisplayActivity
import com.example.intakecare3.roomdatabase.ARG_MED
import com.example.intakecare3.roomdatabase.UserMedicine
import com.example.intakecare3.roomdatabase.UserMedicineDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Timer
import java.util.TimerTask
import kotlin.properties.Delegates

class PharmacyMedicineDetailsActivity : AppCompatActivity() {

    private lateinit var tvMedNameAnswer: TextView
    private lateinit var tvMedDosageAnswer: TextView
    private lateinit var tvNumbDosesAnswer: TextView
    private lateinit var tvNumbTotalDosesAnswer: TextView
    private lateinit var tvProductCode: TextView
    private lateinit var tvExpirationDate: TextView
    private lateinit var tvLotNumber: TextView
    private lateinit var tvSerialNumber: TextView

    private lateinit var btnPrevious : ImageView
    private lateinit var flagRed: ImageView
    private lateinit var flagGreen: ImageView
    private lateinit var flagOrange: ImageView
    private lateinit var flagYellow: ImageView

    private lateinit var flag2Red : ImageView
    private lateinit var flag2Orange: ImageView
    private lateinit var flag2Green: ImageView

    private lateinit var btnTakePill : Button
    private lateinit var btnTakePillOrange : Button
    private lateinit var btnTakePillRed : Button

    private lateinit var expDate: String
    private var medicineTotalNbDosage by Delegates.notNull<Int>()
    private var medicineNbDosage by Delegates.notNull<Int>()
    private var newNumberOfPills by Delegates.notNull<Int>()
    private lateinit var prodCode : String
    private lateinit var userMedicineDatabase: UserMedicineDatabase

    //notification objects
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_medicine_details)

        createNotificationChannel()

        userMedicineDatabase = UserMedicineDatabase.getDatabase(this)

        var intent: Intent = intent
        //get all info from serializable data class with intent and key ARG_MED
        val resultFromPrevious : UserMedicine = intent.getSerializableExtra(ARG_MED) as UserMedicine

        expDate = resultFromPrevious.expDate
        medicineNbDosage = resultFromPrevious.medicineNbDosage
        medicineTotalNbDosage = resultFromPrevious.medicineTotalNbDosage
        prodCode = resultFromPrevious.prodCode
        newNumberOfPills = resultFromPrevious.medicineTotalNbDosage


        tvMedNameAnswer = findViewById(R.id.tvMedNameAnswer)
        tvMedDosageAnswer = findViewById(R.id.tvMedDosageAnswer)
        tvNumbDosesAnswer = findViewById(R.id.tvNumbDosesAnswer)
        tvNumbTotalDosesAnswer = findViewById(R.id.tvNumbTotalDosesAnswer)
        tvProductCode = findViewById(R.id.tvProductCode)
        tvExpirationDate = findViewById(R.id.tvExpirationDate)
        tvLotNumber = findViewById(R.id.tvLotNumber)
        tvSerialNumber = findViewById(R.id.tvSerialNumber)

        btnPrevious = findViewById(R.id.btnPrevious)
        btnTakePill = findViewById(R.id.btnTakePill)
        btnTakePillOrange = findViewById(R.id.btnTakePillOrange)
        btnTakePillRed = findViewById(R.id.btnTakePillRed)

        btnTakePillRed.isInvisible = true
        btnTakePillOrange.isInvisible = true

        flagGreen = findViewById(R.id.btnFlagGreen)
        flagGreen.isInvisible = true
        flagRed = findViewById(R.id.btnFlagRed)
        flagRed.isInvisible = true
        flagOrange = findViewById(R.id.btnFlagOrange)
        flagOrange.isInvisible = true
        flagYellow = findViewById(R.id.btnFlagYellow)
        flagYellow.isInvisible = true

        flag2Green = findViewById(R.id.btnFlagG)
        flag2Green.isInvisible = true
        flag2Orange = findViewById(R.id.btnFlagO)
        flag2Orange.isInvisible = true
        flag2Red = findViewById(R.id.btnFlagR)
        flag2Red.isInvisible = true

        tvMedNameAnswer.text = resultFromPrevious.medicineName
        tvMedDosageAnswer.text = resultFromPrevious.medicineDosage
        tvNumbDosesAnswer.text = resultFromPrevious.medicineNbDosage.toString()
        tvNumbTotalDosesAnswer.text = resultFromPrevious.medicineTotalNbDosage.toString()
        tvProductCode.text = resultFromPrevious.prodCode
        tvExpirationDate.text = resultFromPrevious.expDate
        tvLotNumber.text = resultFromPrevious.ltNumber
        tvSerialNumber.text = resultFromPrevious.serNumber

        checkExpirationDate()

        btnPrevious.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, PharmacyListDisplayActivity::class.java))
        })

        //create notification
        val intentNotif = Intent(this, PharmacyListDisplayActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intentNotif.setAction(Intent.ACTION_MAIN)
        intentNotif.addCategory(Intent.CATEGORY_LAUNCHER)

        val pendingIntent : PendingIntent = PendingIntent.getActivity(this, 0, intentNotif, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Medication Warning!")
            .setContentText("Medication level is below 30%. Make sure to refill!")
            .setSmallIcon(R.drawable.logo_round)
            .setPriority(Notification.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notifManager = NotificationManagerCompat.from(this)

        //Take pill button
        btnTakePill.setOnClickListener(View.OnClickListener {
            takePill()

            updateDatabasePill()

            checkPillStatus()


            val thirtyPercentPills = medicineNbDosage*3
            if (newNumberOfPills < thirtyPercentPills) {
                notifManager.notify(NOTIF_ID, notification)
            }

        })
        checkDoseStatus()

    }

    private fun checkDoseStatus() {
        val fiftyPercentPills = medicineNbDosage*7
        val thirtyPercentPills = medicineNbDosage*3

        //flag only appears after button is pressed; good enough?
        if (newNumberOfPills >= fiftyPercentPills){
            flag2Green.isVisible = true
        }else if (newNumberOfPills in thirtyPercentPills..fiftyPercentPills){
            flag2Orange.isVisible = true
            flag2Green.isInvisible = true
        }else if (newNumberOfPills < thirtyPercentPills){
            flag2Red.isVisible = true
            flag2Green.isInvisible = true
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.GREEN
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun checkPillStatus() {
        val fiftyPercentPills = medicineNbDosage*7
        val thirtyPercentPills = medicineNbDosage*3

        if (newNumberOfPills<= medicineTotalNbDosage && newNumberOfPills == fiftyPercentPills){
            btnTakePillOrange.isInvisible = true
            btnTakePill.isVisible = true
            btnTakePillRed.isInvisible = true
            flag2Green.isVisible = true

        }else if (newNumberOfPills in thirtyPercentPills..fiftyPercentPills){
            btnTakePill.isInvisible = true
            btnTakePillOrange.isVisible = true
            btnTakePillRed.isInvisible = true
            flag2Orange.isVisible = true
            flag2Green.isInvisible = true
            Toast.makeText(this,"Medicine level is 50%!",Toast.LENGTH_SHORT).show()
        }else if (newNumberOfPills < thirtyPercentPills){
            btnTakePill.isInvisible = true
            btnTakePillOrange.isInvisible = true
            btnTakePillRed.isVisible = true
            flag2Red.isVisible = true
            flag2Green.isInvisible = true
            //reminder!
            Toast.makeText(this,"Medicine level is 30%!",Toast.LENGTH_SHORT).show()
        }

    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun updateDatabasePill(){
            var userMedicine : UserMedicine?

        GlobalScope.launch {

            userMedicine = userMedicineDatabase.userMedicineDao().findDosageDetails(prodCode)
            if (prodCode == userMedicine?.prodCode) {
                userMedicineDatabase.userMedicineDao().updatePillNumber(prodCode, newNumberOfPills)
            } else {

            }
        }


}
    private fun takePill() {
        newNumberOfPills = medicineTotalNbDosage - medicineNbDosage
        tvNumbTotalDosesAnswer.text = newNumberOfPills.toString()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkExpirationDate() {
        //expiration date;
        //green -> ok; yellow -> X months to be expired; orange -> last month to be expired
        //red -> expired
        val checkExpirationDate = expDate
        //Date formatter to LocalDate
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val checkExpirationDateFinal = LocalDate.parse(checkExpirationDate, formatter)
        //current date
        val localDate = LocalDate.now()
        val localDate6Months = localDate.minusMonths(6)

        Timer().scheduleAtFixedRate(object : TimerTask() {

            override fun run() {
                if (localDate6Months.isBefore(checkExpirationDateFinal)) {
                    flagGreen.isVisible = true
                    flagOrange.isInvisible = true
                    flagRed.isInvisible = true
                    flagYellow.isInvisible = true

                } else if (checkExpirationDateFinal.isEqual(localDate6Months)) {
                    flagGreen.isInvisible = true
                    flagOrange.isInvisible = true
                    flagRed.isInvisible = true
                    flagYellow.isVisible = true

                } else if (localDate6Months.isAfter(checkExpirationDateFinal) && localDate.isBefore(
                        checkExpirationDateFinal
                    )
                ) {
                    flagGreen.isInvisible = true
                    flagOrange.isVisible = true
                    flagRed.isInvisible = true
                    flagYellow.isInvisible = true

                } else if (localDate.isAfter(checkExpirationDateFinal)) {
                    flagGreen.isInvisible = true
                    flagOrange.isInvisible = true
                    flagRed.isVisible = true
                    flagYellow.isInvisible = true
                }
            }
        }, 0, 100000)
    }
}
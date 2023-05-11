package com.example.intakecare3.therapiesphase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.connectingtomdb.R
import com.example.intakecare3.GUIActivity
import com.example.intakecare3.menuaccess.PharmacyListDisplayActivity
import com.example.intakecare3.menuaccess.QRScannerActivity
import com.example.intakecare3.menuaccess.TherapyLogsActivity
import kotlinx.android.synthetic.main.row_items.*

class TherapyInformationActivity : AppCompatActivity()  {

    private lateinit var drugName : TextView
    private lateinit var posology : TextView
    private lateinit var state : TextView
    private lateinit var startDate : TextView

    private lateinit var scheduleType : TextView
    private lateinit var maxDelay : TextView
    private lateinit var meals : TextView
    private lateinit var validation : TextView
    private lateinit var schTime : TextView
    private lateinit var adherence: TextView

    private lateinit var btnPrevious : ImageView
    private lateinit var btnHome : ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapy_information)

        initData()
    }
private fun initData(){
        drugName = findViewById(R.id.nameOfDrug)
        posology = findViewById(R.id.Posology)
        state = findViewById(R.id.stateOfMedicine)
        startDate = findViewById(R.id.startDate)

        scheduleType = findViewById(R.id.scheduleType)
        maxDelay = findViewById(R.id.maxDelay)
        meals = findViewById(R.id.Meals)
        validation = findViewById(R.id.Validation)
        schTime = findViewById(R.id.schTime)
        adherence = findViewById(R.id.adherencePercent)

        btnPrevious = findViewById(R.id.btnPrevious)
        btnHome = findViewById(R.id.goToHome)

    btnPrevious.setOnClickListener(View.OnClickListener {
        startActivity(Intent(this, TherapyLogsActivity::class.java))
    })
    btnHome.setOnClickListener(View.OnClickListener {
        startActivity(Intent(this, GUIActivity::class.java))
    })

        getData()
    }

    private fun getData(){
        var intent = intent.extras
        //get posology
        var pos1 = intent!!.getString("posology")
        posology.text = pos1
        //get drug
        var dr1 = intent!!.getString("drug")
        drugName.text = dr1
        //get start date
        var sd1 = intent!!.getString("start_date")
        var sd2 = sd1?.substring(0,10)
        startDate.text = "Until: $sd2"
        //get state
        var st1 = intent!!.getBoolean("state")
        if (st1 == true) {
            state.text = "Active"
        }else{
            state.text = "Inactive"
        }
        //get schedule type
        var schT = intent!!.getString("scheduling_type")
        scheduleType.text = schT?.capitalize()
        //get validation
        var valid = intent!!.getString("validation")
        validation.text = valid?.capitalize()
        //get max delay
        var mxD = intent!!.getString("max_delay")
        mxD = mxD?.substringAfter("=")
        maxDelay.text = "Max Delay: $mxD min"
        //get meals
        var mls = intent!!.getString("meals")
        meals.text = "${mls?.capitalize()} Meals"
        //get scheduled time
       var schTm = intent!!.getString("time")
        //did what I had to do
        schTm = schTm?.substring(6,11)
        schTime.text = schTm
        //get adherence
        var adh = intent!!.getInt("adherence")
        adherence.text = "$adh%"

    }
}
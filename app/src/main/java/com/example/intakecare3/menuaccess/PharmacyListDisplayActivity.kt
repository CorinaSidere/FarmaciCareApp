package com.example.intakecare3.menuaccess

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.authentication3.ApiClient
import com.example.connectingtomdb.R
import com.example.intakecare3.GUIActivity
import com.example.intakecare3.loginphase.SessionManager
import com.example.intakecare3.pharmacyphase.EmptyDataObserver
import com.example.intakecare3.pharmacyphase.PharmacyAdapter
import com.example.intakecare3.pharmacyphase.PharmacyIntakesDetails
import com.example.intakecare3.pharmacyphase.PharmacyMedicineDetailsActivity
import com.example.intakecare3.roomdatabase.ARG_MED
import com.example.intakecare3.roomdatabase.UserMedicine
import com.example.intakecare3.roomdatabase.UserViewModel
import kotlinx.android.synthetic.main.activity_pharmacy_list_display.*
import java.util.concurrent.TimeUnit


class PharmacyListDisplayActivity : AppCompatActivity(), PharmacyAdapter.ClickListener {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var pharmacyAdapter: PharmacyAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var toolBar : Toolbar
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    //buttons
    private lateinit var btnPrevious : ImageView
    private lateinit var goToHome : ImageView



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_list_display)

        sessionManager = SessionManager(this)
        apiClient = ApiClient()



        //RecyclerView
        recyclerView = RecyclerView(this)

        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView = findViewById(R.id.recyclerViewPharmacy)
        toolBar = findViewById(R.id.toolBar)

        pharmacyAdapter = PharmacyAdapter(baseContext, this@PharmacyListDisplayActivity)
        pharmacyAdapter.notifyDataSetChanged()
        recyclerView.adapter = pharmacyAdapter

        //Check empty states -> Database is empty screen
      //  val emptyDataObserver = EmptyDataObserver(recyclerView, empty_data_parent)
      //  pharmacyAdapter.registerAdapterDataObserver(emptyDataObserver)

        //UserViewModel
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mUserViewModel.readAllData.observe(this, Observer { user ->
            pharmacyAdapter.setData(user)
        })


     //   periodicTimeWork()

        //button to return to home, and back to medicine details
        btnPrevious = findViewById(R.id.btnPrevious)
        goToHome = findViewById(R.id.goToHome)


        btnPrevious.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, QRScannerActivity::class.java))
        })
        goToHome.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,GUIActivity::class.java))
        })

    }


   /* private fun periodicTimeWork(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myWorkRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,1, TimeUnit.HOURS
        ).setConstraints(constraints)
            .addTag("Medicine levels calculation")
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("Medicine levels calculation", ExistingPeriodicWorkPolicy.KEEP, myWorkRequest)
    }*/

    override fun clickedItem(userMedicine: UserMedicine) {
        //the more button;  //get all info from serializable data class with intent and key ARG_MED
        startActivity(Intent(this,PharmacyMedicineDetailsActivity::class.java).putExtra(ARG_MED,userMedicine))

    }

}
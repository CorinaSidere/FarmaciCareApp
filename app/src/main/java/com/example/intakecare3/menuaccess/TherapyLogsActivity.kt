package com.example.intakecare3.menuaccess

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.authentication3.ApiClient
import com.example.connectingtomdb.R
import com.example.intakecare3.GUIActivity
import com.example.intakecare3.loginphase.SessionManager
import com.example.intakecare3.therapiesphase.TherapyAdapter
import com.example.intakecare3.therapiesphase.TherapyDetails
import com.example.intakecare3.therapiesphase.TherapyInformationActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TherapyLogsActivity : AppCompatActivity(), TherapyAdapter.ClickedListener {
    private lateinit var sessionManager : SessionManager
    private lateinit var therapyAdapter: TherapyAdapter
    private lateinit var apiClient: ApiClient
    private lateinit var btnPrevious : ImageView
    //define variables
    private lateinit var toolBar : Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapy_logs)

        sessionManager = SessionManager(this)
        apiClient = ApiClient()
        recyclerView = RecyclerView(this)


        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager


        toolBar = findViewById(R.id.toolBar)
        recyclerView = findViewById(R.id.recyclerView)

        getListOfTherapies()


        //back button
        btnPrevious = findViewById(R.id.btnPrevious)
        btnPrevious.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, GUIActivity::class.java))
        })



    }

    fun getListOfTherapies(){
        //GET list of therapies
        apiClient.getTherapyService(this).getTherapy().enqueue(object: Callback<List<TherapyDetails>>{
            override fun onResponse(
                call: Call<List<TherapyDetails>>,
                response: Response<List<TherapyDetails>>
            ) {
                if (response.isSuccessful){
                    val therapyResponse : List<TherapyDetails> = response.body()!!

                    therapyAdapter = TherapyAdapter(baseContext, therapyResponse, this@TherapyLogsActivity)
                    therapyAdapter.notifyDataSetChanged()
                    recyclerView.adapter = therapyAdapter

                }
                Log.d("SUCCESS", response.body().toString())
            }

            override fun onFailure(call: Call<List<TherapyDetails>>, t: Throwable) {
                Log.e("FAILURE", t.localizedMessage)
            }

        })
    }

    override fun clickedItem(therapyModel: TherapyDetails) {
        //transform that amazing ArrayList into something doable
        var maxDelay = therapyModel.delivery?.options.toString().split(",")[1]
        println(maxDelay)
        var schTime = therapyModel.delivery?.options.toString().split(",")[2]
        println(schTime)

        //.putExtra(THERAPY_KEY,therapyModel) put also this bc I need the full ID; this can be improved tbh
        startActivity(Intent(this, TherapyInformationActivity::class.java).putExtra("posology", therapyModel.posology).putExtra("drug", therapyModel.drug)
            .putExtra("start_date", therapyModel.startDate).putExtra("state", therapyModel.state).putExtra("meals",therapyModel.meals)
            .putExtra("scheduling_type",therapyModel.delivery?.schedulingType).putExtra("max_delay", maxDelay)
            .putExtra("validation",therapyModel.validation).putExtra("time",schTime).putExtra("adherence", therapyModel.adherence))
    }
}
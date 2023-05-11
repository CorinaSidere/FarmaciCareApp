package com.example.intakecare3.menuaccess

import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.work.*

import androidx.work.ListenableWorker.Result.Success
import com.example.authentication3.ApiClient
import com.example.connectingtomdb.R
import com.example.intakecare3.loginphase.SessionManager
import com.example.intakecare3.pharmacyphase.PharmacyIntakesDetails
import com.example.intakecare3.roomdatabase.*
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.jvm.internal.Intrinsics.Kotlin


class PharmacyListActivity : AppCompatActivity() {

    private lateinit var remainingString : String
    private lateinit var tvproductCode : String
    private lateinit var tvexpirationDate : String
    private lateinit var tvlotNumber : String
    private lateinit var tvserialNumber : String
    private lateinit var btnSaveInformation : Button

    private lateinit var etMedicineName : EditText
    private lateinit var etMedicineDose : EditText
    private lateinit var etMedicineNbDosage: EditText
    private lateinit var etMedicineTotalNbDosage : EditText

    private lateinit var btnCheckInformation : Button

    //final variables for the codes
    private lateinit var expDate : String
    private lateinit var finalExpdate : String
    private lateinit var finalLotnumber : String
    private lateinit var finalSNnumber : String

    //initialize ViewModel
    private lateinit var mUserViewModel : UserViewModel
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient
    private lateinit var pharmacyIntakesDetails: PharmacyIntakesDetails
    private lateinit var productCode : String
    private lateinit var userMedicineDatabase: UserMedicineDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_list)
        userMedicineDatabase = UserMedicineDatabase.getDatabase(this)
        sessionManager = SessionManager(this)
        apiClient = ApiClient()

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        var intent: Intent = intent
        var theResult: String? = intent.getStringExtra("Result")
        productCode = theResult!!.substring(3, 17)
        Log.d("IMPORTANT", "Product code is: $productCode")
        val endSubstring = theResult.length
        remainingString = theResult.substring(17, endSubstring)
        Log.d("IMPORTANT", "Remaining string is: $remainingString")
        //apply regex function for scanning the QR and obtaining the codes
        applyRegex()


        //+medicine form for completion
        etMedicineName = findViewById(R.id.medNameAnswer)
        etMedicineDose = findViewById(R.id.medDosageAnswer)
        etMedicineNbDosage = findViewById(R.id.numbDosesAnswer)
        etMedicineTotalNbDosage = findViewById(R.id.numbTotalDosesAnswer)
        btnSaveInformation = findViewById(R.id.btnSaveInformation)
        btnCheckInformation = findViewById(R.id.btnCheckInformation)

        //check if data already exists inside the DB with the Check button; if any information
        //must be modified, then the text fields are editable and then the info can be saved(and replaced
        // inside the db); if no info must be modified, then just press SAVE to go on
        btnCheckInformation.setOnClickListener(View.OnClickListener {
               checkData()
        })

        //do date conversion
        dateConversion()

        tvproductCode = productCode
        tvlotNumber = finalLotnumber
        tvserialNumber = finalSNnumber
        tvexpirationDate = expDate

        //get PharmacyIntakesDetails from website
      //  getPharmacyIntakesDetails()

        btnSaveInformation.setOnClickListener(View.OnClickListener {
            insertDataToDatabase()
        })

    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun checkData(){
        val prodCode = productCode

        var userMedicine : UserMedicine?

    GlobalScope.launch {
        userMedicine = userMedicineDatabase.userMedicineDao().findDosageDetails(prodCode)
        if (prodCode == userMedicine?.prodCode) {
            etMedicineName.setText(userMedicine?.medicineName)
            etMedicineDose.setText(userMedicine?.medicineDosage)
            etMedicineNbDosage.setText(userMedicine?.medicineNbDosage.toString())
            etMedicineTotalNbDosage.setText(userMedicine?.medicineTotalNbDosage.toString())
        } else {

        }
    }
    }

    //function does not work anymore due to the changes of the InTakeCare platform
    private fun getPharmacyIntakesDetails() {
        //GET intakes for patient; we select the STATUS for running in the background ->
        // if it's "taken" -> take the medicineNbDosage from PharmacyListActivity and take away 1
        apiClient.getPharmacyIntakesService(this).getIntakes().enqueue(object:
            Callback<List<PharmacyIntakesDetails>> {
            override fun onResponse(
                call: Call<List<PharmacyIntakesDetails>>,
                response: Response<List<PharmacyIntakesDetails>>
            ) {
                if (response.isSuccessful){
                     val pharmacyIntakesResponse : List<PharmacyIntakesDetails> = response.body()!!

                    val pharmacyIntakesDetailsProgrammedDate = pharmacyIntakesResponse.get(1).programmedDate.toString()

                  /*  val medicineValues3 = OneTimeWorkRequest.Builder(MyWorker::class.java)
                    val data3 = Data.Builder()
                    data3.putString("programmedDate", pharmacyIntakesDetailsProgrammedDate)
                    medicineValues3.setInputData(data3.build())
                    WorkManager.getInstance(this@PharmacyListActivity).enqueue(medicineValues3.build())

                  val pharmacyIntakesDetailsStatus = pharmacyIntakesResponse.get(3).status.toString()

                    val medicineValues4 = OneTimeWorkRequest.Builder(MyWorker::class.java)
                    val data4 = Data.Builder()
                    data4.putString("status", pharmacyIntakesDetailsStatus)
                    medicineValues4.setInputData(data4.build())
                    WorkManager.getInstance(this@PharmacyListActivity).enqueue(medicineValues4.build())*/

                }
                Log.d("SUCCESS", response.body().toString())

            }
            override fun onFailure(call: Call<List<PharmacyIntakesDetails>>, t: Throwable) {
                Log.e("FAILURE", t.localizedMessage)
            }

        })
    }

    private fun insertDataToDatabase() {
        val prodCode = tvproductCode
        val expDate = expDate
        val ltNumber = tvlotNumber
        val serNumber = tvserialNumber

        val medicineName = etMedicineName.text.toString()
        val medicineDose = etMedicineDose.text.toString()
        val medicineNbDosage = etMedicineNbDosage.text.toString().toInt()
        val medicineTotalNbDosage = etMedicineTotalNbDosage.text.toString().toInt()


        if (inputCheck(medicineName,medicineDose, medicineNbDosage, medicineTotalNbDosage)){
            //Create User Object; id starts from 0 bc id is autogenerated; modified also UserMedicine with LocalDate!!!
            val userMedicine = UserMedicine(0, medicineName,medicineDose,medicineNbDosage,medicineTotalNbDosage,prodCode, expDate, ltNumber, serNumber)
            //Add Data to Database
            mUserViewModel.addUserMedicine(userMedicine)


            //part for calculation of medicine intakes; does not work;
          /*  val medicineValues1 = OneTimeWorkRequest.Builder(MyWorker::class.java)
            val data1 = Data.Builder()
            data1.putString("medicineNbDosage", userMedicine.medicineNbDosage.toString())
            medicineValues1.setInputData(data1.build())
            WorkManager.getInstance(this).enqueue(medicineValues1.build())


            val medicineValues2 = OneTimeWorkRequest.Builder(MyWorker::class.java)
            val data2 = Data.Builder()
            data2.putString("medicineTotalNbDosage", userMedicine.medicineTotalNbDosage.toString())
            medicineValues2.setInputData(data2.build())
            WorkManager.getInstance(this).enqueue(medicineValues2.build())*/


            Toast.makeText(this,"Successfully added!", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, PharmacyListDisplayActivity::class.java))
        }else{
            Toast.makeText(this,"Empty fields!", Toast.LENGTH_LONG).show()
        }

    }


    private fun inputCheck(medicineName : String, medicineDose: String, medicineNbDosage: Int, medicineTotalNbDosage: Int): Boolean{
        return !(TextUtils.isEmpty(medicineName) && TextUtils.isEmpty(medicineDose) && TextUtils.isEmpty(medicineNbDosage.toString()) && TextUtils.isEmpty(medicineTotalNbDosage.toString()))

    }

    private fun applyRegex() {
        //try with combinations
        // is here because that's what android studio sees
        //first combination (01-/17-10-21) if(whole regex is...)
        val regex1 = "17\\d{6}10[A-z0-9]+\u001D21[A-z0-9]+".toRegex()
        val regex2 = "21[A-z0-9]+\u001D17\\d{6}10[A-z0-9]+".toRegex()
        val regex3 = "17\\d{6}21[A-z0-9]+\u001D10[A-z0-9]+".toRegex()

        if (regex1.matches(remainingString)) {
            val EXPpattern = "17\\d{6}10".toRegex()
            val expirationDate = EXPpattern.find(remainingString)?.value?.split(remainingString)
             //type: List<String>
            Log.d("IMPORTANT", expirationDate.toString())
            //text processing for UI
            val separator1 = " "
            val expdate1 = expirationDate!!.joinToString(separator1)
            println(expdate1)
            finalExpdate =  expdate1.substring(2,8)
            println(finalExpdate)


            var LOTpattern = "10[A-z0-9]+\u001D21".toRegex()
            val lotNumber = LOTpattern.find(remainingString)?.value?.split(remainingString)
            Log.d("IMPORTANT", lotNumber.toString())
            val separatorLOT1 = " "
            val lotnumb1 = lotNumber!!.joinToString(separatorLOT1)
            println(lotnumb1)
            finalLotnumber = lotnumb1.substring(2,9)
            println(finalLotnumber)

            var SNpattern = "21[A-z0-9]+".toRegex()
            val snNumber = SNpattern.find(remainingString)?.value?.split(remainingString)
            Log.d("IMPORTANT", snNumber.toString())
            val separatorSN1 = " "
            val snnumb1 = snNumber!!.joinToString(separatorSN1)
            println(snnumb1)
            finalSNnumber = snnumb1.substring(2,snnumb1.length)
            println(finalSNnumber)

        } else {
            println("Pattern 1 not recognized")
        }

        if (regex2.matches(remainingString)) {
            //second combination(01-/21-17-10)
            val EXPpattern2 = "\u001D17\\d{6}10".toRegex()
            val expirationDate2 = EXPpattern2.find(remainingString)?.value?.split(remainingString)
            println(expirationDate2) //type: List<String>
            Log.d("IMPORTANT", expirationDate2.toString())
            val separator2 = " "
            val expdate2 = expirationDate2!!.joinToString(separator2)
            println(expdate2)
            finalExpdate = expdate2.substring(3,9)
            println(finalExpdate)

            var LOTpattern2 = "10[A-z0-9]+".toRegex()
            val lotNumber2 = LOTpattern2.find(remainingString)?.value?.split(remainingString)
            Log.d("IMPORTANT", lotNumber2.toString())
            val separatorLOT2 = " "
            val lotnumb2 = lotNumber2!!.joinToString(separatorLOT2)
            println(lotnumb2)
            finalLotnumber = lotnumb2.substring(2,lotnumb2.length)
            println(finalLotnumber)

            var SNpattern2 = "21[A-z0-9]+\u001D17".toRegex()
            val snNumber2 = SNpattern2.find(remainingString)?.value?.split(remainingString)
            Log.d("IMPORTANT", snNumber2.toString())
            val separatorSN2 = " "
            val snnumb2 = snNumber2!!.joinToString(separatorSN2)
            println(snnumb2)
            finalSNnumber = snnumb2.substring(2,14)
            println(finalSNnumber)

        } else {
            println("Pattern 2 not recognized")
        }
        if (regex3.matches(remainingString)) {
            //third combination(01-/17-21-10)
            val EXPpattern3 = "17\\d{6}21".toRegex()
            val expirationDate3 = EXPpattern3.find(remainingString)?.value?.split(remainingString)
            println(expirationDate3) //type: List<String>
            Log.d("IMPORTANT", expirationDate3.toString())
            val separator3 = " "
            val expdate3 = expirationDate3!!.joinToString(separator3)
            println(expdate3)
            finalExpdate = expdate3.substring(2,8)
            println(finalExpdate)

            var LOTpattern3 = "\u001D10[A-z0-9]+".toRegex()
            val lotNumber3 = LOTpattern3.find(remainingString)?.value?.split(remainingString)
            Log.d("IMPORTANT", lotNumber3.toString())
            val separatorLOT3 = " "
            val lotnumb3 = lotNumber3!!.joinToString(separatorLOT3)
            println(lotnumb3)
            finalLotnumber = lotnumb3.substring(3,lotnumb3.length)
            println(finalLotnumber)

            var SNpattern3 = "21[A-z0-9]+\u001D10".toRegex()
            val snNumber3 = SNpattern3.find(remainingString)?.value?.split(remainingString)
            Log.d("IMPORTANT", snNumber3.toString())
            val separatorSN3 = " "
            val snnumb3 = snNumber3!!.joinToString(separatorSN3)
            println(snnumb3)
            finalSNnumber = snnumb3.substring(2,18)
            println(finalSNnumber)
        } else {
            println("Pattern 3 not recognized")
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
   fun dateConversion(){
    val dateString = finalExpdate

    val stringWithSpaceAfterEvery2ndChar = dateString.replace("..".toRegex(), "$0 ")
    println(stringWithSpaceAfterEvery2ndChar)

    //add the year that will always be the same..
    val newString = "20".plus(stringWithSpaceAfterEvery2ndChar)
    val newString2 = newString.trim()
    //println(newString2)

    //add -
    val regexDate = "\\s".toRegex()

    expDate = regexDate.replace(newString2, "\\-")
   // println(expDate)

}



    }


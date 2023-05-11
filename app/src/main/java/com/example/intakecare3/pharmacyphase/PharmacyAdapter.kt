package com.example.intakecare3.pharmacyphase

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.connectingtomdb.R
import com.example.intakecare3.roomdatabase.UserMedicine
import com.example.intakecare3.therapiesphase.TherapyAdapter
import kotlinx.android.synthetic.main.row_items_pharmacy.view.*
import org.w3c.dom.Text

class PharmacyAdapter(val context: Context, clicksListener: ClickListener) : RecyclerView.Adapter<PharmacyAdapter.PharmacyAdapterVH>() {

    private var userMedicineList = emptyList<UserMedicine>()
    private var clicksListener: ClickListener = clicksListener

    class PharmacyAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var medicineName : TextView
        var medicineDose : TextView
        var medicineNbDosage : TextView
        var theLogo: ImageView
        var itemMore : ImageView

        init {
            medicineName = itemView.findViewById(R.id.tvMedicineName)
            medicineDose = itemView.findViewById(R.id.tvMedicineDose)
            medicineNbDosage = itemView.findViewById(R.id.tvMedicineNbDosage)
            theLogo = itemView.findViewById(R.id.theLogo)
            itemMore = itemView.findViewById(R.id.btnItemMore)
        }

    }

    fun setData(userMedicine: List<UserMedicine>){
        this.userMedicineList = userMedicine
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyAdapterVH {
        return PharmacyAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.row_items_pharmacy,parent,false)
        )
    }

    override fun onBindViewHolder(holder: PharmacyAdapterVH, position: Int) {
        val currentItem = userMedicineList[position]

        holder.medicineName.text = currentItem.medicineName
        holder.medicineDose.text = currentItem.medicineDosage
        holder.medicineNbDosage.text = currentItem.medicineNbDosage.toString()
        holder.itemView.setOnClickListener {
            clicksListener.clickedItem(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return userMedicineList.size
    }
    interface ClickListener {
        fun clickedItem(currentItem: UserMedicine)

    }

}




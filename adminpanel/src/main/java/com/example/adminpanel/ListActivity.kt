package com.example.adminpanel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.adminpanel.databinding.ActivityListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects


class ListActivity : AppCompatActivity() {

    private lateinit var dbref: DatabaseReference
    private lateinit var vehicleRecyclerView: RecyclerView
    private lateinit var vehicleArrayList: ArrayList<VehicleData>
     private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vehicleRecyclerView = binding.recviewList
        vehicleRecyclerView.layoutManager = LinearLayoutManager(this)
        vehicleRecyclerView.setHasFixedSize(true)


        vehicleArrayList = arrayListOf<VehicleData>()
        getVehicleData()


    }



    private fun getVehicleData() {
        dbref = FirebaseDatabase.getInstance().getReference("Vehicle Information")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (vehicleSnap in snapshot.children) {
                        val vehicle = vehicleSnap.getValue(VehicleData::class.java)
                        vehicleArrayList.add(vehicle!!)
                    }

                    val myAdapter = MyAdapter(vehicleArrayList,
                        updateListener = { vehicleNumber -> onUpdateItemClicked(vehicleNumber) },
                        deleteListener = { vehicleNumber -> onDeleteItemClicked(vehicleNumber) })

                    vehicleRecyclerView.adapter = myAdapter
                    myAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }








    private fun onUpdateItemClicked(vehicleNumber: String) {
        // Find the selected vehicle data from the vehicleArrayList
        val selectedVehicle = vehicleArrayList.find { it.vehicleNumber == vehicleNumber }
        if (selectedVehicle != null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Update Vehicle Info")

            // Inflating the layout for the AlertDialog
            val view = LayoutInflater.from(this).inflate(R.layout.activity_update, null)
            builder.setView(view)

            // EditText fields in the layout
            val vehicleBrand = view.findViewById<EditText>(R.id.vehicleBrand)
            val ownerName = view.findViewById<EditText>(R.id.vehicleOwnerName)
            val vehicleRTO = view.findViewById<EditText>(R.id.vehicleRTO)
            val vehNumber = view.findViewById<TextView>(R.id.vehNumber)

            // Set the existing data to EditText fields for user reference
            vehicleBrand.setText(selectedVehicle.vehicleBrand)
            ownerName.setText(selectedVehicle.ownerName)
            vehicleRTO.setText(selectedVehicle.vehicleRTO)
            vehNumber.setText(selectedVehicle.vehicleNumber)
            vehNumber.isFocusable = false
            vehNumber.isClickable = false

            // Set positive button for update
            builder.setPositiveButton("Update") { _, _ ->
                // Get the updated values from EditText fields
                val updatedVehicleBrand = vehicleBrand.text.toString()
                val updatedOwnerName = ownerName.text.toString()
                val updatedVehicleRTO = vehicleRTO.text.toString()

                // Update the data in Firebase using vehicleNumber
                updateDataInFirebase(vehicleNumber, updatedVehicleBrand,updatedVehicleRTO,updatedOwnerName)
            }

            // Set negative button for cancel
            builder.setNegativeButton("Cancel") { _, _ ->

            }

            builder.show()
        }


    }

    private fun updateDataInFirebase(
        vehicleNumber: String,
        updatedVehicleBrand: String,
        updatedVehicleRTO: String,
        updatedOwnerName: String
    ) {
        // Update the data in Firebase using vehicleNumber
        dbref.child(vehicleNumber).updateChildren(
            mapOf(
                "vehicleBrand" to updatedVehicleBrand,
                "vehicleRTO" to updatedVehicleRTO,
                "ownerName" to updatedOwnerName
            )
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Toast.makeText(this@ListActivity, "Updated Successfully", Toast.LENGTH_SHORT).show()
                recreate()
            } else {
                Toast.makeText(this@ListActivity, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }










    private fun onDeleteItemClicked(vehicleNumber: String) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure to delete this vehicle ?")

        builder.setPositiveButton("Yes") { _, _ ->
            // Kullanıcı "Yes" dediğinde yapılacak işlemler
            dbref = FirebaseDatabase.getInstance().getReference("Vehicle Information")
            dbref.child(vehicleNumber).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val position = findPositionByVehicleNumber(vehicleNumber)
                    if (position != -1) {
                        vehicleArrayList.removeAt(position)
                    vehicleRecyclerView.adapter?.notifyItemRemoved(position)
                    Toast.makeText(this@ListActivity, "Deleted Successfully", Toast.LENGTH_SHORT)
                        .show()

                    }

                } else {
                    Toast.makeText(this@ListActivity, task.exception.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        builder.setNegativeButton("No"){_,_ ->
        }

        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()

    }






    private fun findPositionByVehicleNumber(vehicleNumber: String): Int {
        for ((index, vehicle) in vehicleArrayList.withIndex()) {
            if (vehicle.vehicleNumber == vehicleNumber) {
                return index
            }
        }
        return -1
    }




}
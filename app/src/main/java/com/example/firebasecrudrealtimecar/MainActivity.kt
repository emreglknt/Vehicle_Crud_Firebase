package com.example.firebasecrudrealtimecar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasecrudrealtimecar.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {


    private lateinit var binding :ActivityMainBinding
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchButton.setOnClickListener{
            val searchVehicleNumber :String = binding.searchVehicle.text.toString()

            if(searchVehicleNumber.isNotEmpty()){
                readData(searchVehicleNumber)
                binding.searchVehicle.text.clear()
            }
            else{
                Toast.makeText(this,"Please Enter the Vehicle Number",Toast.LENGTH_LONG).show()
            }

        }

    }


    private fun readData(vehicleNumber: String){

        dbReference = FirebaseDatabase.getInstance().getReference("Vehicle Information")
        dbReference.child(vehicleNumber).get().addOnSuccessListener {

        if(it.exists()){

            val ownerName =it.child("ownerName").value
            val vehicleBrand =it.child("vehicleBrand").value
            val vehicleRTO =it.child("vehicleRTO").value
            Toast.makeText(this,"Results Found",Toast.LENGTH_SHORT).show()
            binding.ownerNameTxt.text=ownerName.toString()
            binding.vehiclebrandtext.text=vehicleBrand.toString()
            binding.VehicleRtoText.text=vehicleRTO.toString()



        }
            else{
            Toast.makeText(this,"Sorry,Result Not Found ",Toast.LENGTH_LONG).show()
        }

        }.addOnFailureListener{
            Toast.makeText(this,"Something went wrong !! ",Toast.LENGTH_LONG).show()
        }

    }





}
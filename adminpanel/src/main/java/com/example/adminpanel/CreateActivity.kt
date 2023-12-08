package com.example.adminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.adminpanel.databinding.ActivityCreateBinding
import com.example.adminpanel.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding
    private lateinit var dbReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createSaveButton.setOnClickListener{

            val ownerName = binding.createOwnerName.text.toString()
            val vehicleBrand = binding.createBrand.text.toString()
            val vehicleRTO = binding.vehicleRTO.text.toString()
            val vehicleNumber = binding.vehicleNumber.text.toString()

            dbReference = FirebaseDatabase.getInstance().getReference("Vehicle Information")
            val vehicleData = VehicleData(ownerName,vehicleBrand,vehicleRTO,vehicleNumber)

           //child içerisine vehicleNumber gönderdik çnkü unique bir parametre
            dbReference.child(vehicleNumber).setValue(vehicleData).addOnSuccessListener {

                //edittext fieldlarını temizle
                binding.createOwnerName.text.clear()
                binding.createBrand.text.clear()
                binding.vehicleRTO.text.clear()
                binding.vehicleNumber.text.clear()

                Toast.makeText(this,"Vehicle Saved",Toast.LENGTH_LONG).show()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener{

                Toast.makeText(this,"Vehicle Saved",Toast.LENGTH_LONG).show()

            }




        }
    }
}
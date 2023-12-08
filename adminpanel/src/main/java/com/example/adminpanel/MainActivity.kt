package com.example.adminpanel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminpanel.databinding.ActivityCreateBinding
import com.example.adminpanel.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.Create.setOnClickListener{
            val intent=Intent(this@MainActivity,CreateActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.vehiclelist.setOnClickListener{
            val intent = Intent(this@MainActivity,ListActivity::class.java)
            startActivity(intent)
        }



    }

}
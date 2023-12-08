package com.example.adminpanel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adminpanel.databinding.RecyclerItemBinding

class MyAdapter(val vehicleList : ArrayList<VehicleData>, private val updateListener: (String) -> Unit,
                private val deleteListener: (String) -> Unit)
    : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    inner class MyViewHolder(private val binding: RecyclerItemBinding):RecyclerView.ViewHolder(binding.root){
        val vehicleBrand : TextView = binding.recViewBrand
        val vehicleOwner :TextView = binding.recViewOwner
        val vehicleNumber: TextView = binding.recVehicleNumber
        val vehicleRTO : TextView = binding.recVehicleRTO
        val updateButton :Button = binding.update
        val deleteButton :Button = binding.delete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vehicleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = vehicleList[position]

        holder.vehicleBrand.text = currentItem.vehicleBrand
        holder.vehicleOwner.text = currentItem.ownerName
        holder.vehicleNumber.text = currentItem.vehicleNumber
        holder.vehicleRTO.text = currentItem.vehicleRTO

        holder.updateButton.setOnClickListener {
            updateListener.invoke(currentItem.vehicleNumber!!)
        }

        holder.deleteButton.setOnClickListener {
            deleteListener.invoke(currentItem.vehicleNumber!!)
        }



    }
}
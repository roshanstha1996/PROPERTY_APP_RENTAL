package com.example.property_app_g01.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.property_app_g01.databinding.ViewProperRowLayoutBinding
import com.example.property_app_g01.interfaces.ClickDetectorInterface
import com.example.property_app_g01.models.PropertyDetail


class MyAdapter(var myItems:MutableList<PropertyDetail>,
                val clickInterface: ClickDetectorInterface
) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ViewProperRowLayoutBinding) : RecyclerView.ViewHolder (binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewProperRowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    // 3. How many items are in the list of data?
    override fun getItemCount(): Int {
        return myItems.size
    }


    // In a single row, what data goes in the <TextView>?
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currItem: PropertyDetail = myItems.get(position)

        Log.d("TESTING 1", "test")
        Log.d("TESTING 1", currItem.toString())
        // put the item into the row_layout.xml UI elements
        holder.binding.tvRow1.text = currItem.streetAddress
        holder.binding.tvRow2.text = "${currItem.bedroomCount} bedrooms"
//        if (currItem.isElectric) {
//            holder.binding.tvRow3.text = "Is Electric? Yes"
//        } else {
//            holder.binding.tvRow3.text = "Is Electric? No"
//        }

        // click handlers
        holder.binding.btnDelete.setOnClickListener {
            clickInterface.deleteRow(position)
        }

        holder.binding.root.setOnClickListener {
            clickInterface.rowClicked(position)
        }
    }

}
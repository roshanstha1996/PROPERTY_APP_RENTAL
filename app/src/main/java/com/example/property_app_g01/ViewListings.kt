package com.example.property_app_g01

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.property_app_g01.adapters.MyAdapter
import com.example.property_app_g01.databinding.ActivityViewListingsBinding
import com.example.property_app_g01.interfaces.ClickDetectorInterface
import com.example.property_app_g01.models.PropertyDetail
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ViewListings : AppCompatActivity(), ClickDetectorInterface {

    lateinit var binding: ActivityViewListingsBinding
    lateinit var adapter:MyAdapter

    // TODO: Create a Firebase auth variable
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore


    var dataToDisplay:MutableList<PropertyDetail> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewListingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // TODO: Initialize Firebase auth
        // initialize Firebase auth
        auth = Firebase.auth

        // -------- recyclerView
        adapter = MyAdapter(dataToDisplay, this)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

//        val snackbar = Snackbar.make(binding.root, "getting property details!", Snackbar.LENGTH_SHORT)
//        snackbar.show()
        getRentalPropertiesDetails()

        // TODO: Add code for customizing the App Bar
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Landlord App")

        }
    }


    override fun rowClicked(position: Int) {
        val selectedCar:PropertyDetail = dataToDisplay.get(position)
//        binding.etModel.setText(selectedCar.model)
//        binding.etOwner.setText(selectedCar.owner)
//        binding.etLicensePlate.setText(selectedCar.licensePlate)
//        binding.swIsElectric.isChecked = selectedCar.isElectric

        // populate the invisible edittext with the document id so we can use it to update
//        binding.etDocumentID.setText(selectedCar.docId)
    }


    override fun deleteRow(position: Int) {
        // 1. Get the document id of the selected car
        val selectedCar:PropertyDetail = this.dataToDisplay.get(position)
        // 2. Execute the Firestore request to do the delete
//        db.collection("cars")
//            .document(selectedCar.docId)
//            .delete()
//            .addOnSuccessListener {
//                Log.d("TESTING", "Document deleted!")
//                // refresh the recyclerview
//
//
////                if (this.loggedInUserIsAdmin == true) {
////                    this.getAllDocuments()
////                } else {
////                    this.getCarsByOwner(auth.currentUser!!.uid)
////                }
//
//            }
//            .addOnFailureListener {
//                    e ->
//                Log.w("TESTING", "Error deleting document", e)
//            }

    }

    fun getRentalPropertiesDetails() {
        db.collection("propertyDetail")
            .whereEqualTo("ownerId", auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                    results: QuerySnapshot ->


                val snackbar = Snackbar.make(binding.root, "getting property details!", Snackbar.LENGTH_SHORT)
                snackbar.show()

                // create a temporary list of cars
                val propertiesFromDB:MutableList<PropertyDetail> = mutableListOf()
                // for each document in the collection,
                for (document in results) {
                    val currentProperty:PropertyDetail = document.toObject(PropertyDetail::class.java)

                    Log.d("TESTING 1", currentProperty.toString())
                    propertiesFromDB.add(currentProperty)
                }
                // display your cars
                this.dataToDisplay.clear()
                this.dataToDisplay.addAll(propertiesFromDB)
                this.adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("TESTING", "Error getting documents.", exception)
            }


    }


}
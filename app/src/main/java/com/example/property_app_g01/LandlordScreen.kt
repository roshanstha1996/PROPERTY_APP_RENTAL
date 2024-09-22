package com.example.property_app_g01

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g01.databinding.ActivityLandlordScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class LandlordScreen : AppCompatActivity() {

    lateinit var binding: ActivityLandlordScreenBinding

    // TODO: Create a Firebase auth variable
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandlordScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Initialize Firebase auth
        // initialize Firebase auth
        auth = Firebase.auth

        // TODO: Add code for customizing the App Bar
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Landlord App")

            //shows the back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish();
                return true;
            }
            R.id.mi_createListings -> {
                val intent = Intent(this@LandlordScreen, CreateListings::class.java)
//                intent.putExtra("KEY_IS_ADDING", true)
                startActivity(intent)
                return true
            }
            R.id.mi_viewListings -> {
                val intent = Intent(this@LandlordScreen, ViewListings::class.java)
                startActivity(intent)
                return true
            }
            R.id.mi_logout ->{
                logoutCurrentUser()
//                this.finish();
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun logoutCurrentUser() {
        // TODO: Code to logout
        Firebase.auth.signOut()
    }
}
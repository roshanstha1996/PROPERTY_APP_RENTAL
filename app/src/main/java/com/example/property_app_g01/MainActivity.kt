package com.example.property_app_g01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g01.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // TODO: Create a Firebase auth variable
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // TODO: Initialize Firebase auth
        // initialize Firebase auth
        auth = Firebase.auth

        // TODO: Add code for customizing the App Bar
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Landlord App")

        }

        // click handlers
        binding.btnLogin.setOnClickListener {
            // get email and password
            val emailFromUI = binding.txtUserName.text.toString()
            val passwordFromUI = binding.txtPassword.text.toString()
            // try to login
            loginUser(emailFromUI, passwordFromUI)
        }
    }

    // helper functions for Firebase Auth
    fun loginUser(email:String, password:String): Boolean {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TEST_USER", "signInWithEmail:success")


                    val intent = Intent(this@MainActivity, LandlordScreen::class.java)
    //                intent.putExtra("KEY_IS_ADDING", true)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TEST_USER", "signInWithEmail:failure", task.exception)
//                    binding.tvResults.setText("ERROR: Login failed, check logcat for reasons")
//                    return@addOnCompleteListener
                }
            }

        return false
    }
}

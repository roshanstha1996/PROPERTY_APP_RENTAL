package com.example.property_app_g01

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.LocationRequest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.property_app_g01.databinding.ActivityCreateListingsBinding
import com.example.property_app_g01.models.Geo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale


class CreateListings : AppCompatActivity() {

    lateinit var mMap: GoogleMap

    private val TAG:String = "TESTING"

    lateinit var binding: ActivityCreateListingsBinding

    // TODO: Create a Firebase auth variable
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    // add property for the geocoder class
    lateinit var geocoder: Geocoder

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val APP_PERMISSIONS_LIST =arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
    )

    private val requestLocationPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
        // results list is going to contain an objects that shows you what the user picked for each permission
            resultsList:Map<String, Boolean> ->


        var x:Boolean = true
        for (item in resultsList.entries) {
            if (item.key in APP_PERMISSIONS_LIST && item.value == false) {
                x = false// Checking if the current permission is set to false
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateListingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Initialize Firebase auth
        // initialize Firebase auth
        auth = Firebase.auth

        // TODO: Add code for customizing the App Bar
        if (supportActionBar != null) {
            supportActionBar!!.setTitle("Create Listings")

            //shows the back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        }

        // instantiate the class
        geocoder = Geocoder(applicationContext, Locale.getDefault())

        // initialize FusedLocationProvider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // click handlers
        binding.btnGetCoordinates.setOnClickListener {
            getCoordinates()
        }


        binding.btnGetStreetAddress.setOnClickListener {
            getStreetAddress()
        }

        binding.btnGetCurrentLocation.setOnClickListener {
            requestLocationPermissionsLauncher.launch(APP_PERMISSIONS_LIST)

            findMyLocation()
        }

        binding.btnPostListings.setOnClickListener {
            postListings()
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    // helper functions
    private fun getCoordinates() {
        // 1. get address from text box
        val addressFromUI = binding.etStreetAddress.text.toString()
        Log.d(TAG, "Getting coordinates for ${addressFromUI}")

        // attempt to get coordinates
        try {
            val searchResults:MutableList<Address>? =
                geocoder.getFromLocationName(addressFromUI, 1)

            // 3. The function can return 1 of 3 possible results
            // So you must handle all possible results


            // 3a. case #1 - result is null
            if (searchResults == null) {
                Snackbar.make(binding.root, "Not able to get location of the address or the coordinates", Snackbar.LENGTH_SHORT).show()
                // do not proceed, and exit
                return
            }


            // 3b. case #2 - result is an empty array
            if (searchResults.isEmpty()) {

                Snackbar.make(binding.root, "Not able to get location of the address or the coordinates", Snackbar.LENGTH_SHORT).show()
                return
            }


            // 3c. case #3 - result contains an array with values
            // It will return an array with 1 object inside
            val matchingItem: Address = searchResults[0]


            val output = "Coordinate found: (${matchingItem.latitude}, ${matchingItem.longitude})"

            Log.e(TAG + "test", "coordinates : ${output}")
            binding.etLat.setText(matchingItem.latitude.toString())
            binding.etLng.setText(matchingItem.longitude.toString())

        } catch(ex:Exception) {
            Log.e(TAG, "Error encountered while getting coordinates")
            Log.e(TAG, ex.toString())
        }
    }

    private fun getStreetAddress() {
        // 1. get the lat/lng from the UI
        val latFromUI = binding.etLat.text.toString()
        val lngFromUI = binding.etLng.text.toString()


        // 2. Convert string values to doubles
        val latAsDouble = latFromUI.toDouble()
        val lngAsDouble = lngFromUI.toDouble()


        if(latAsDouble < -90 || latAsDouble > 90){
            Snackbar.make(binding.root, "Range for latitude should be between -90 to 90", Snackbar.LENGTH_SHORT).show()
            return
        }

        if(lngAsDouble < -180 || lngAsDouble > 180){
            Snackbar.make(binding.root, "Range for longitude should be between -180 to 180", Snackbar.LENGTH_SHORT).show()
            return
        }

        Log.d("TESTING", "Getting address for ${latAsDouble}, ${lngAsDouble}")

        // attempt to get the address
        try {
            val searchResults:MutableList<Address>? =
                geocoder.getFromLocation(latAsDouble, lngAsDouble, 1)



            // 3a. case #1 - result is null
            if (searchResults == null) {
                Snackbar.make(binding.root, "Not able to get location of the address or the coordinates", Snackbar.LENGTH_SHORT).show()
                return
            }


            // 3b. case #2 - result is an empty array
            if (searchResults.isEmpty()) {
                Snackbar.make(binding.root, "Not able to get location of the address or the coordinates", Snackbar.LENGTH_SHORT).show()
                return
            }


            // 3c. case #3 - result contains an array with values
            // It will return an array with 1 object inside
            val matchingItem: Address = searchResults[0]


            val countryName = matchingItem.countryName
            val locality = matchingItem.locality
            val streetAddress = matchingItem.thoroughfare
            val city = matchingItem.locality
            val province = matchingItem.adminArea


            Log.d(TAG, "locality: ${locality}")
            Log.d(TAG, "Address: ${streetAddress}")
            Log.d(TAG, "City: ${city}")
            Log.d(TAG, "Province: ${province}")
            Log.d(TAG, "Country: ${countryName}")

            val output1 = """
                        ${locality} ${streetAddress}, ${city}, ${province}, ${countryName}""".trimIndent()

            binding.etStreetAddress.setText(output1)

        } catch(ex:Exception) {
            Log.e(TAG, "Error encountered while getting street address.")
            Log.e(TAG, ex.toString())
        }

    }

    @SuppressLint("MissingPermission")
    fun findMyLocation() {


        val cancellationTokenSource = CancellationTokenSource()


        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_BALANCED_POWER_ACCURACY, cancellationTokenSource.token)
            .addOnSuccessListener { location ->

                binding.etLat.setText(location.latitude.toString())
                binding.etLng.setText(location.longitude.toString())

                try {
                    val searchResults:MutableList<Address>? =
                        geocoder.getFromLocation(location.latitude.toDouble(), location.longitude.toDouble(), 1)


                    // 3a. case #1 - result is null
                    if (searchResults == null) {
                        // do not proceed, and exit
                        return@addOnSuccessListener
                    }

                    // 3b. case #2 - result is an empty array
                    if (searchResults.isEmpty() == true) {
                        return@addOnSuccessListener
                    }

                    // 3c. case #3 - result contains an array with values
                    // It will return an array with 1 object inside
                    val matchingItem: Address = searchResults[0]


                    val countryName = matchingItem.countryName
                    val locality = matchingItem.locality
                    val streetAddress = matchingItem.thoroughfare
                    val city = matchingItem.locality
                    val province = matchingItem.adminArea


                    Log.d(TAG, "locality: ${locality}")
                    Log.d(TAG, "Address: ${streetAddress}")
                    Log.d(TAG, "City: ${city}")
                    Log.d(TAG, "Province: ${province}")
                    Log.d(TAG, "Country: ${countryName}")

                    val output1 = """
                        ${locality} ${streetAddress}, ${city}, ${province}, ${countryName}""".trimIndent()

                    binding.etStreetAddress.setText(output1)

                } catch(ex:Exception) {
                    Log.e(TAG, "Error encountered while getting street address.")
                    Log.e(TAG, ex.toString())
                }
            }
            .addOnFailureListener { exception ->

                Snackbar.make(binding.root, "ERROR in getting the current location address :${exception}", Snackbar.LENGTH_SHORT).show()
            }
    }

    fun postListings() {

        // get values from form
        val addressVal:String = binding.etStreetAddress.text.toString()
        val latitude:String = binding.etLat.text.toString()
        val longitude:String = binding.etLng.text.toString()
        val imageURL:String = binding.txtImageURL.text.toString()
        val monthlyRent:String = binding.txtMonthlyRent.text.toString()
        val bedroomCount:String = binding.bedroomCount.text.toString()

        //getting the current logged in user id
        val ownerId:String = auth.currentUser!!.uid

        val geo: Geo = Geo(latitude, longitude)
        val address = com.example.property_app_g01.models.Address(addressVal, geo)

        // TODO: create a data structure with the data to insert
        val data: MutableMap<String, Any> = HashMap();
        data["streetAddress"] = addressVal
        data["lat"] = latitude
        data["lng"] = longitude
        data["imageURL"] = imageURL
        data["monthlyRent"] = monthlyRent
        data["bedroomCount"] = bedroomCount
        data["isRented"] = false
        data["ownerId"] = ownerId


        // insert the data into the collection
        db.collection("propertyDetail")
            .add(data)
            .addOnSuccessListener { docRef ->
                Log.d("TESTING", "Document successfully added with ID : ${docRef.id}")

                val snackbar = Snackbar.make(binding.root, "Property details added to your listings!", Snackbar.LENGTH_SHORT)
                snackbar.show()

                resetAllFormFields()
            }
            .addOnFailureListener { ex ->
                Log.e("TESTING", "Exception ocurred while adding a document : $ex", )


                val snackbar = Snackbar.make(binding.root, "Property details failed to be added !", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
    }


    fun resetAllFormFields()  {
        binding.etStreetAddress.setText("")
        binding.etLat.setText("")
        binding.etLng.setText("")
        binding.txtImageURL.setText("")
        binding.txtMonthlyRent.setText("")
        binding.bedroomCount.setText("")
    }
}

package com.example.lab_restaurantes_yuritzy_rodriguez

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    private val service by lazy { RetrofitProvider.retrofit.create(RestaurantsApi::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)
        createFragment()

        intent.extras?.let {
            val id = it.get("ID") as Int

            service.getRestaurantById(1).enqueue(object: Callback<Restaurant> {
                override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                    val restaurant = response.body()

                    Glide.with(this@RestaurantDetailActivity).load(restaurant?.photoOne).into(findViewById(R.id.imageView_detail_restaurant))
                    Glide.with(this@RestaurantDetailActivity).load(restaurant?.photoTwo).into(findViewById(R.id.imageView_detail_restaurant_two))
                    Glide.with(this@RestaurantDetailActivity).load(restaurant?.photoThree).into(findViewById(R.id.imageView_detail_restaurant_three))
                    findViewById<TextView>(R.id.textView_detail_name).text = restaurant?.name
                    findViewById<TextView>(R.id.textView_detail_grade).text = restaurant?.grade.toString()
                    findViewById<TextView>(R.id.textView_detail_year).text = restaurant?.year.toString()
                    findViewById<TextView>(R.id.textView_detail_cost).text = restaurant?.cost.toString()
                    findViewById<TextView>(R.id.textView_detail_review).text = restaurant?.review
                    findViewById<TextView>(R.id.textView_detail_address).text = restaurant?.address

                    zoom(restaurant?.lat?: 0.0, restaurant?.lng?: 0.0, restaurant?.name?: "")
                }

                override fun onFailure(call: Call<Restaurant>, t: Throwable) {

                }

            })
        }
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            //mostarr ventana de permiso
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map=p0

        enableLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    private fun enableLocation(){
        if(!::map.isInitialized)return

        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled=true
        } else{
            requestLocationPermission()
        }
    }

    private fun zoom(x: Double, y: Double, name: String) {
        val coordinates = LatLng(x, y)
        val marker= MarkerOptions().position(coordinates).title(name)

        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates,20f), 4000,null)
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

}
package com.nice.tracker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    val stopName = "STOP"
    val startName = "START"
    val timeName = "Time: "
    val distanceName = "Distance: "
    val speedName = "Speed: "
    val latName = "Latitude: "
    val longName = "Longitude: "
    var isON = false
    val FULL_STOP = 0
    val REFRESH = 1
    var difference: Long = 0L
    val sdf = SimpleDateFormat("HH:mm:ss")
    val MY_PERMISSIONS_ACCESS_LOCATION = 251

    var permissionOK = false

    //var counter = 0
    //private lateinit var locationManager: LocationManager
    //private lateinit var locationListener: LocationListener
    private lateinit var locationResult: Task<Location>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logTV.setText("checking location")

        locationResult = LocationServices.getFusedLocationProviderClient(this).lastLocation

/*       fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            // Got last known location. In some rare situations this can be null.
            logTV.setText("location null")
            if (location != null) {
                logTV.setText("location exists!")
            }
        }*/

/*        val locationRequest : LocationRequest
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        val locationCallback : LocationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                if (p0 == null) return
                for (location in p0.getLocations()) {
                    if (location != null) {
                        var lat = location.latitude
                        var long = location.longitude
                        logTV.setText("coord = $lat , $long")
                    }
                }
            }
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())*/


/*        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                counter++
                latTV.setText(latName + location.latitude)
                longTV.setText(longName + location.longitude)
                logTV.setText("counter = $counter")
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
            override fun onProviderEnabled(s: String) {}
            override fun onProviderDisabled(s: String) {}
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager*/

        sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
        helloTV.setText("hi!")
        startButton.setText(startName)

        startButton.setOnClickListener {
            if (isON) {
                isON = false
            } else {
                isON = true
                go()
            }
        }

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            permissionOK = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_PERMISSIONS_ACCESS_LOCATION
            )
        }

/*        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            logTV.setText("permissionGranted")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2000,
                10.0F, locationListener
            )
        }*/
    }

    open fun getDeviceLocation() {
        if (permissionOK) {
            val locationResult =
                LocationServices.getFusedLocationProviderClient(this).lastLocation
            locationResult.addOnCompleteListener {

                if (it.isSuccessful)
                    it.result?.let { location ->
                        latTV.setText(latName + location.latitude)
                        longTV.setText(longName + location.longitude)
                        logTV.setText("location success")
                    }
                else
                    logTV.setText("location fail")
            }
        } else {
            logTV.setText("location fail")
        }
    }


    fun go() {
        difference = 0L
        startButton.setText(stopName)
        helloTV.setText("go!")
        var startTime: Long = System.currentTimeMillis()
        var distance = 0.0F
        var oldLocation = Location("")
        oldLocation.latitude = 0.0
        oldLocation.longitude = 0.0

        val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    FULL_STOP -> {
                        difference += System.currentTimeMillis() - startTime
                        startButton.setText(startName)


                    }
                    REFRESH -> {
                        var permissionGranted = false
                        var locExists = false
                        timeTV.setText(timeName + sdf.format(difference + System.currentTimeMillis() - startTime))
                        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, locationListener)
                        if (ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                                this@MainActivity,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            permissionGranted = true
                            locationResult.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    it.result?.let { location ->
                                        locExists = true
                                        if (oldLocation.latitude != 0.0) {

                                            latTV.setText(latName + location.latitude)
                                            longTV.setText(longName + location.longitude)

                                            var path = oldLocation.distanceTo(location)
                                            oldLocation = location
                                            distance += path

                                            val speed = distance / (difference / 1000.0)
                                            speedTV.setText(speedName + speed)
                                            distanceTV.setText(distanceName + distance)
                                        }
                                    } ?: run {
                                        permissionGranted = false
                                    }

                                    logTV.setText("permission granted = $permissionGranted , locExists = $locExists")
                                }

                            }
                        }
                        //logTV.setText("permission granted = $permissionGranted , locExists = $locExists")

                    }
                }
            }
        }

        val thread1 = Thread(Runnable {
            try {
                while (isON) {
                    TimeUnit.MILLISECONDS.sleep(500)
                    handler.sendEmptyMessage(REFRESH)
                }
                handler.sendEmptyMessage(FULL_STOP)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        thread1.start()
    }
}



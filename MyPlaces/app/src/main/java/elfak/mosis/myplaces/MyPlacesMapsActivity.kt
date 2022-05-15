package elfak.mosis.myplaces

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import elfak.mosis.myplaces.databinding.ActivityMyPlacesMapsBinding

class MyPlacesMapsActivity : AppCompatActivity(), OnMapReadyCallback
{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMyPlacesMapsBinding
    private var state = 0
    private var selCoorsEnabled = false
    private lateinit var placeLoc : LatLng
    private var markerPlaceIdMap = HashMap<Marker,Int>()


    companion object
    {
        val NEW_PLACE = 1
        val PERMISSION_ACCESS_FINE_LOCATION = 1
        val SHOW_MAP = 0
        val CENTER_PLACE_ON_MAP = 1
        val SELECT_CORDINATES = 1
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        try
        {
            var mapIntent = intent
            var mapBundle = mapIntent.extras

            state = mapBundle?.getInt("state") ?: 0
            if(state == CENTER_PLACE_ON_MAP)
            {
                var lat = mapBundle?.getString("lat") ?: "0"
                var lon = mapBundle?.getString("lon") ?: "0"
                placeLoc = LatLng(lat.toDouble(), lon.toDouble())
            }
        }
        catch (e:Exception)
        {
            Log.d("Error", "Error Reading State")
        }

        binding = ActivityMyPlacesMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var fab:FloatingActionButton = findViewById(R.id.fab)
        if (state != SELECT_CORDINATES )
        {
            fab.setOnClickListener { view ->
                var i: Intent = Intent(this, EditMyPlaceActivity::class.java)
                startActivityForResult(i, NEW_PLACE)
            }
        }
        else
        {
            var layout : ViewGroup = fab.parent as ViewGroup
            layout?.removeView(fab)
        }



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        if(state == SELECT_CORDINATES && !selCoorsEnabled)
        {
            menu?.add(0,1,1,"Select Coordinates")
            menu?.add(0,2,2,"Cancel")
            return super.onCreateOptionsMenu(menu)
        }
        menuInflater.inflate(R.menu.menu_my_places_maps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        var id = item.itemId
        if (state == SELECT_CORDINATES && !selCoorsEnabled)
        {
            when(id)
            {   1 ->
                {
                    selCoorsEnabled = true
                    Toast.makeText(this, "Select coordinates", Toast.LENGTH_SHORT).show()
                }
                2 ->
                {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
        }
        else
        {
            when (id)
            {
                R.id.new_place_item ->
                {
                    var i = Intent(this, EditMyPlaceActivity::class.java)
                    startActivityForResult(i, 1)
                }
                R.id.about_item ->
                {
                    var i = Intent(this, About::class.java)
                    startActivity(i)
                }
                android.R.id.home -> finish()

            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ACCESS_FINE_LOCATION)
        }
        else
        {
            when (state)
            {
                SHOW_MAP -> mMap.isMyLocationEnabled = true
                CENTER_PLACE_ON_MAP -> setOnMapClickListener()
                else -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15 as Float))
            }
            addMyPlaceMarkers()
        }
    }

    private fun addMyPlaceMarkers()
    {
        var places = MyPlacesData.myPlaces
        for ((i, place) in places.withIndex())
        {
            var lat = place.latitude
            var lon = place.longitude
            var loc = LatLng(lat.toDouble(),lon.toDouble())

            var markerOptions = MarkerOptions()
            markerOptions.position(loc)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
            markerOptions.title(place.name)

            var marker = mMap.addMarker(markerOptions)
            if(marker != null)
                markerPlaceIdMap.put(marker, i)

            mMap?.setOnMarkerClickListener {  it ->
                var intent = Intent(this,ViewMyPlacesActivity::class.java)
                var i = markerPlaceIdMap[it]
                intent.putExtra("position",i)
                startActivity(intent)
                true
            }
        }
    }

    private fun setOnMapClickListener()
    {
        mMap?.setOnMapClickListener { it ->
            if (state == SELECT_CORDINATES && selCoorsEnabled)
            {
                var lat = it.latitude.toBigDecimal().toPlainString()
                var lon = it.longitude.toBigDecimal().toPlainString()
                var i = Intent()
                i.putExtra("lon", lon)
                i.putExtra("lat", lat)
                setResult(Activity.RESULT_OK, i)
                finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            PERMISSION_ACCESS_FINE_LOCATION ->
            {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    when (state)
                    {
                        SHOW_MAP -> mMap.isMyLocationEnabled = true
                        CENTER_PLACE_ON_MAP -> setOnMapClickListener()
                        else -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLoc, 15 as Float))
                    }
                    addMyPlaceMarkers()
                }
            }
        }
    }
}
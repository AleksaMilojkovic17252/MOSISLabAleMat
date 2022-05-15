package elfak.mosis.myplaces

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import elfak.mosis.myplaces.databinding.ActivityEditMyPlaceBinding

class EditMyPlaceActivity : AppCompatActivity()
{
    var editMode:Boolean = true
    var position:Int = -1

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityEditMyPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityEditMyPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try
        {
            val listIntent:Intent = intent
            val positionBundle: Bundle? = listIntent.extras
            if(positionBundle != null)
            {
                position = positionBundle.getInt("position")
            }
            else
                editMode = false
        }
        catch(e:Exception)
        {
            editMode = false
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        var fja = { view : View ->
            when(view.id)
            {
                R.id.editmyplace_finished_button ->
                {
                    var etName: EditText = findViewById<EditText>(R.id.editmyplace_name_edit)
                    var nme: String = etName.text.toString()
                    var etDesc: EditText = findViewById<EditText>(R.id.editmyplace_desc_edit)
                    var desc: String = etDesc.text.toString()
                    var etLat: EditText = findViewById<EditText>(R.id.editmyplace_lat_edit)
                    var lat: String = etLat.text.toString()
                    var etLong: EditText = findViewById<EditText>(R.id.editmyplace_lon_edit)
                    var long: String = etLong.text.toString()

                    if(!editMode)
                    {
                        val place: MyPlace = MyPlace(nme,desc,long, lat)
                        MyPlacesData.addNewPlace(place)
                    }
                    else
                    {
                        val place:MyPlace = MyPlacesData.getPlace(position)
                        place.name = nme
                        place.description = desc
                        place.longitude = long
                        place.latitude = lat
                    }


                    setResult(Activity.RESULT_OK)
                    finish()
                }
                R.id.editmyplace_cancel_button ->
                {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
                R.id.editmyplace_location_button ->
                {
                    var i = Intent(this, MyPlacesMapsActivity::class.java)
                    i.putExtra("state",MyPlacesMapsActivity.SELECT_CORDINATES)
                    startActivityForResult(i,1)
                }
            }
        }

        var nameEditText: EditText = findViewById<EditText>(R.id.editmyplace_name_edit)
        var finishedButton: Button = findViewById<Button>(R.id.editmyplace_finished_button)
        if(!editMode)
        {
            finishedButton.isEnabled = false
            finishedButton.setText("Add")
        }
        else if(position >= 0)
        {
            finishedButton.setText("Save")
            val place:MyPlace = MyPlacesData.getPlace(position)
            nameEditText.setText(place.name)
            val descEditText: EditText = findViewById<EditText>(R.id.editmyplace_desc_edit)
            descEditText.setText(place.description)

        }
        finishedButton.setOnClickListener(fja)
        finishedButton.isEnabled = false
        finishedButton.text = "Add"

        var cancelButton: Button = findViewById<Button>(R.id.editmyplace_cancel_button)
        cancelButton.setOnClickListener(fja)


        nameEditText.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }

            override fun afterTextChanged(p0: Editable?)
            {
                finishedButton.isEnabled = p0?.isNotEmpty() ?: false
            }

        })

        var locationButton: Button = findViewById(R.id.editmyplace_location_button)
        locationButton.setOnClickListener(fja)
    }

    override fun onSupportNavigateUp(): Boolean
    {
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.menu_edit_my_place, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        var id = item.itemId
        when(id)
        {
            R.id.action_show_map -> Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show()
            R.id.action_my_places_list ->
            {
                val i: Intent = Intent(this, MyPlacesList::class.java)
                startActivity(i)
            }
            R.id.action_about ->
            {
                val i : Intent = Intent(this,About::class.java)
                startActivity(i)
            }
            android.R.id.home -> finish()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        try
        {
            when(resultCode)
            {
                Activity.RESULT_OK ->
                {
                    var lon = data?.extras?.getString("lon")
                    var etLon = findViewById<EditText>(R.id.editmyplace_lon_edit)
                    etLon.setText(lon)

                    var lat = data?.extras?.getString("lat")
                    var etLat = findViewById<EditText>(R.id.editmyplace_lat_edit)
                    etLat.setText(lat)

                }
            }
        }
        catch (e : Exception)
        {

        }
    }
}
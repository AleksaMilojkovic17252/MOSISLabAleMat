package elfak.mosis.myplaces

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import elfak.mosis.myplaces.databinding.ActivityViewMyPlacesBinding
import java.lang.Exception

class ViewMyPlacesActivity : AppCompatActivity()
{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityViewMyPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityViewMyPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        var pos:Int = -1

        try
        {
            val listIntent:Intent = intent
            val bdl:Bundle? = listIntent.extras
            pos = bdl?.getInt("position") ?: -1
        }
        catch (e:Exception)
        {
            Toast.makeText(this, e.message,Toast.LENGTH_SHORT).show()
            finish();
        }

        if(pos >= 0)
        {
            val place:MyPlace = MyPlacesData.getPlace(pos)
            val twName: TextView = findViewById<TextView>(R.id.viewmyplace_name_text)
            twName.setText(place.name)
            val twDesc: TextView = findViewById<TextView>(R.id.viewmyplace_desc_text)
            twDesc.setText(place.description)
            val finishedButton: Button = findViewById<Button>(R.id.viewmyplace_finished_button)
            finishedButton.setOnClickListener { view ->
                finish()
            }

        }



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

    override fun onSupportNavigateUp(): Boolean
    {
        return super.onSupportNavigateUp()
    }
}
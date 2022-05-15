package elfak.mosis.myplaces

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import elfak.mosis.myplaces.databinding.ActivityMyPlacesListBinding

class MyPlacesList : AppCompatActivity()
{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMyPlacesListBinding
    private var places: ArrayList<String> = ArrayList<String>()

    companion object
    {
        val NEW_PLACE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPlacesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var listaMesta:ListView = findViewById(R.id.my_places_list)
        MyPlacesData.popuni()
        listaMesta.adapter = ArrayAdapter<MyPlace>(this,android.R.layout.simple_list_item_1,MyPlacesData.myPlaces)
        listaMesta.setOnItemClickListener{ adapterView, view, i, l ->
            val positionBundle:Bundle = Bundle()
            positionBundle.putInt("position",i)
            val radi:Intent = Intent(this, ViewMyPlacesActivity::class.java)
            radi.putExtras(positionBundle)
            startActivity(radi)
        }

        listaMesta.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
            val info: AdapterView.AdapterContextMenuInfo = contextMenuInfo as AdapterView.AdapterContextMenuInfo
            val place: MyPlace = MyPlacesData.getPlace(info.position)
            contextMenu.setHeaderTitle(place.name)
            contextMenu.add(0,1,1,"View Place")
            contextMenu.add(0,2,2,"Edit Place")
            contextMenu.add(0,3,3,"Delete Place")
            contextMenu.add(0,4,4,"Show on Map")
        }


        binding.fab.setOnClickListener { view ->
            val i: Intent = Intent(this, EditMyPlaceActivity::class.java)
            startActivityForResult(i,NEW_PLACE)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info: AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val positionBundle:Bundle = Bundle()
        positionBundle.putInt("position",info.position)
        var i:Intent? = null
        when(item.itemId)
        {
            1 ->
            {
                i = Intent(this, ViewMyPlacesActivity::class.java)
                i.putExtras(positionBundle)
                startActivity(i)
            }
            2 ->
            {
                i =  Intent(this, EditMyPlaceActivity::class.java)
                i.putExtras(positionBundle)
                startActivityForResult(i,1)
            }
            3 ->
            {
                MyPlacesData.deletePlace(info.position)
                setList()
            }
             4 ->
             {
                 i = Intent(this, MyPlacesMapsActivity::class.java)
                 i.putExtra("state",MyPlacesMapsActivity.CENTER_PLACE_ON_MAP)
                 var place = MyPlacesData.myPlaces[info.position]
                 i.putExtra("lat",place.latitude)
                 i.putExtra("lon",place.longitude)
                 startActivityForResult(i,2)
             }
        }
        return super.onContextItemSelected(item)
    }

    private fun setList()
    {
        var myPlaceListView = findViewById<ListView>(R.id.my_places_list)
        myPlaceListView.adapter = ArrayAdapter<MyPlace>(this, android.R.layout.simple_list_item_1, MyPlacesData.myPlaces)
    }

    override fun onSupportNavigateUp(): Boolean
    {
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.menu_my_places_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        when(item.itemId)
        {
            R.id.action_show_map -> Toast.makeText(this, "Show Map!", Toast.LENGTH_SHORT).show()
            R.id.action_new_place ->
            {
                var i: Intent = Intent(this,EditMyPlaceActivity::class.java)
                startActivityForResult(i, MainActivity.NEW_PLACE)
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
        if (resultCode == Activity.RESULT_OK)
        {
            var listaMesta:ListView = findViewById(R.id.my_places_list)
            MyPlacesData.popuni()
            listaMesta.adapter = ArrayAdapter<MyPlace>(this,android.R.layout.simple_list_item_1,MyPlacesData.myPlaces)
            Toast.makeText(this, "New Place Added", Toast.LENGTH_SHORT).show()
        }
    }
}
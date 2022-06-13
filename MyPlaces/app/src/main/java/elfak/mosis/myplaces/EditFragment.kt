package elfak.mosis.myplaces

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import elfak.mosis.myplaces.data.MyPlace
import elfak.mosis.myplaces.model.LocationViewModel
import elfak.mosis.myplaces.model.MyPlacesViewModel

class EditFragment : Fragment() {

    private val myPlacesViewModel: MyPlacesViewModel by activityViewModels()
    private val locationViewModel: LocationViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        if (myPlacesViewModel.selected == null)
            (activity as AppCompatActivity).supportActionBar?.title = "Add My Place"
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        
        val editName = requireView().findViewById<EditText>(R.id.editmyplace_name_edit)
        val editDesc = requireView().findViewById<EditText>(R.id.editmyplace_desc_edit)

        val editLongitude = requireView().findViewById<EditText>(R.id.editmyplace_longitude_edit)
        val lonObserver = Observer<String>
        {
            editLongitude.setText(it.toString())
        }
        locationViewModel.longitude.observe(viewLifecycleOwner, lonObserver)

        val editLatitude = requireView().findViewById<EditText>(R.id.editmyplace_latitude_edit)
        val latObserver = Observer<String>
        {
            editLatitude.setText(it.toString())
        }
        locationViewModel.latitude.observe(viewLifecycleOwner, latObserver)

        if (myPlacesViewModel.selected != null)
        {
            editName.setText(myPlacesViewModel.selected?.name)
            editDesc.setText(myPlacesViewModel.selected?.description)

        }
        val addButton: Button = requireView().findViewById<Button>(R.id.editmyplace_finished_button)
        addButton.isEnabled = false
        if (myPlacesViewModel.selected != null)
            addButton.setText(R.string.editmyplace_save_label)

        editName.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?)
            {
                addButton.isEnabled = (editName.text.length > 0)
            }

        })

        addButton.setOnClickListener {
            val name: String = editName.text.toString()
            val desc: String = editDesc.text.toString()
            val longitude: String = editLongitude.text.toString()
            val latitude: String = editLatitude.text.toString()
            if (myPlacesViewModel.selected != null)
            {
                myPlacesViewModel.selected?.name = name
                myPlacesViewModel.selected?.description = desc
                myPlacesViewModel.selected?.longitude = longitude
                myPlacesViewModel.selected?.latitude = latitude
            }
            else
                myPlacesViewModel.addPlace(MyPlace(name,desc,longitude,latitude))

            myPlacesViewModel.selected = null
            locationViewModel.setLocation("","")
            findNavController().popBackStack()
        }

        val cancelButton: Button = requireView().findViewById<Button>(R.id.editmyplace_cancel_button)
        cancelButton.setOnClickListener {
            myPlacesViewModel.selected = null
            locationViewModel.setLocation("","")
            findNavController().popBackStack()
        }

        val setButton = requireView().findViewById<Button>(R.id.editmyplace_location_button)
        setButton.setOnClickListener {
            locationViewModel.setLocation = true
            findNavController().navigate(R.id.action_EditFragment_to_MapFragment)
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        //myPlacesViewModel.selected = null
    }

}
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
import androidx.navigation.fragment.findNavController
import elfak.mosis.myplaces.data.MyPlace
import elfak.mosis.myplaces.model.MyPlacesViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {

    private val myPlacesViewModel: MyPlacesViewModel by activityViewModels();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_my_places_list -> {
                this.findNavController().navigate(R.id.action_EditFragment_to_ListFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu)
    {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_new_place)
        item.isVisible = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
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
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }

            override fun afterTextChanged(p0: Editable?)
            {
                addButton.isEnabled = (editName.text.length > 0)
            }

        })

        addButton.setOnClickListener {
            val name: String = editName.text.toString()
            val desc: String = editDesc.text.toString()
            if (myPlacesViewModel.selected != null)
            {
                myPlacesViewModel.selected?.name = name
                myPlacesViewModel.selected?.description = desc
            }
            else
                myPlacesViewModel.addPlace(MyPlace(name,desc))
            findNavController().navigate(R.id.action_EditFragment_to_ListFragment)
        }

        val cancelButton: Button = requireView().findViewById<Button>(R.id.editmyplace_cancel_button)
        cancelButton.setOnClickListener {
            findNavController().navigate((R.id.action_EditFragment_to_ListFragment))
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        myPlacesViewModel.selected = null
    }

}
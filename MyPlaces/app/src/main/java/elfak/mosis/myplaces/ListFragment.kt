package elfak.mosis.myplaces

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import elfak.mosis.myplaces.data.MyPlace
import elfak.mosis.myplaces.databinding.FragmentListBinding
import elfak.mosis.myplaces.model.MyPlacesViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val myPLacesViewModel: MyPlacesViewModel by activityViewModels();

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_my_places_list)
        item.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId){
            R.id.action_new_place -> {
                this.findNavController().navigate(R.id.action_ListFragment_to_EditFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val listaMesta: ListView = requireView().findViewById<ListView>(R.id.my_places_list)
        listaMesta.adapter = ArrayAdapter<MyPlace>(view.context,android.R.layout.simple_list_item_1,myPLacesViewModel.myPlacesList)
        listaMesta.setOnItemClickListener { adapterView, view, i, l ->
            var myPlace = adapterView?.adapter?.getItem(i) as MyPlace
            myPLacesViewModel.selected = myPlace
            view.findNavController().navigate(R.id.action_ListFragment_to_ViewFragment)
        }
        listaMesta.setOnCreateContextMenuListener { contextMenu, view, contextMenuInfo ->
            val info = contextMenuInfo as AdapterView.AdapterContextMenuInfo
            var myPlace = myPLacesViewModel.myPlacesList[info.position]
            contextMenu.setHeaderTitle(myPlace.name)
            contextMenu.add(0, 1, 1, "View Place")
            contextMenu.add(0, 2, 2, "Edit Place")
            contextMenu.add(0, 1, 1, "Delete Place")

        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean
    {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        when(item.itemId)
        {
            1 ->
            {
                myPLacesViewModel.selected = myPLacesViewModel.myPlacesList[info.position]
                this.findNavController().navigate(R.id.action_ListFragment_to_ViewFragment)
            }
            2 ->
            {
                myPLacesViewModel.selected = myPLacesViewModel.myPlacesList[info.position]
                this.findNavController().navigate(R.id.action_ListFragment_to_EditFragment)
            }
            3 ->
            {
                myPLacesViewModel.myPlacesList.removeAt(info.position)
                val myPlaceList = requireView().findViewById<ListView>(R.id.my_places_list)
                myPlaceList.adapter = this@ListFragment.context?.let { ArrayAdapter<MyPlace>(it, android.R.layout.simple_list_item_1,myPLacesViewModel.myPlacesList) }
            }

        }

        return super.onContextItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
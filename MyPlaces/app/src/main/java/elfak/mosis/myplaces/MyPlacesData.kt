package elfak.mosis.myplaces

object MyPlacesData
{
    var myPlaces: ArrayList<MyPlace> = ArrayList<MyPlace>()


    fun addNewPlace(place: MyPlace) = myPlaces.add(place)
    fun getPlace(index: Int) = myPlaces[index]
    fun deletePlace(index: Int) = myPlaces.removeAt(index)
    fun popuni()
    {
        if(myPlaces.isEmpty())
        {
            myPlaces.add(MyPlace("Park Svetog Save"))
            myPlaces.add(MyPlace("Tvrdjava"))
            myPlaces.add(MyPlace("Cair"))
            myPlaces.add(MyPlace("Park Svetog Jovana"))
            myPlaces.add(MyPlace("Trg Kralja Milana"))
            myPlaces.add(MyPlace("Jagodin Mala"))
        }
    }

}
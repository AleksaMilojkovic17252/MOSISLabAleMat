package elfak.mosis.myplaces.data

data class MyPlace(var name:String, var description:String, var longitude: String, var latitude: String)
{
    override fun toString(): String = name
}

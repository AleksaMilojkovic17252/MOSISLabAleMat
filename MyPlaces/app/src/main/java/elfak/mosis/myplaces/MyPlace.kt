package elfak.mosis.myplaces

class MyPlace(n:String, desc:String = "Opis", long:String = "0", lang:String = "0")
{
    var name: String =n
    var description: String = desc
    var longitude: String = long
    var latitude: String = lang
    var ID: Int = 0

    override fun toString(): String = name
}
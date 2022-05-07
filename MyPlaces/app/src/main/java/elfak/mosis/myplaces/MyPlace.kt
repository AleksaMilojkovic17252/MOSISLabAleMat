package elfak.mosis.myplaces

class MyPlace(n:String, desc:String = "Opis")
{
    var name: String =n
    var description: String = desc

    override fun toString(): String = name
}
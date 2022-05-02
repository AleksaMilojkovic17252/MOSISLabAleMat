package elfak.mosis.labvezba1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val finishButton: Button = findViewById(R.id.finish_button)
        finishButton.setOnClickListener({view -> finish()})
        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show()
    }

    override fun onStart()
    {
        super.onStart()
        Toast.makeText(this,"onStart",Toast.LENGTH_SHORT).show()
    }

    override fun onRestart()
    {
        super.onRestart()
        Toast.makeText(this,"onRestart",Toast.LENGTH_SHORT).show()
    }

    override fun onResume()
    {
        super.onResume()
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show()
    }

    override fun onPause()
    {
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show()
        super.onPause()
    }

    override fun onStop()
    {
        Toast.makeText(this,"onStop",Toast.LENGTH_SHORT).show()
        super.onStop()
    }

    override fun onDestroy()
    {
        Toast.makeText(this,"onDestroy",Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}
package email.storage.osama.storageapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_home.*

class Home : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_home)

        add.setOnClickListener {
    startActivity(Intent(this, Add::class.java))
      }

        show.setOnClickListener {
            startActivity(Intent(this, Show::class.java))
        }


    }
}

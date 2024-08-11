package com.lu.init

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.lu.init.frag.OptionFirstFrag
import com.lu.init.frag.OptionSecondFrag

class MainActivity : AppCompatActivity() {

    val optionFirstFrag: OptionFirstFrag = OptionFirstFrag()
    val optionSecondFrag: OptionSecondFrag = OptionSecondFrag()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etMessage: EditText = findViewById(R.id.messageET)
        val btnSend: Button = findViewById(R.id.btnSend)
        val context = this

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, optionFirstFrag)
            .addToBackStack(null)
            .commit()

        btnSend.setOnClickListener {
            val message = etMessage.text.toString()
            if (message == null || message.isBlank()) {
                Toast.makeText(context, "Please enter a message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, DisplayActivity::class.java)
            intent.putExtra(MSG_KEY, message)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.item1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, optionFirstFrag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show()
            }

            R.id.item2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, optionSecondFrag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    companion object {
        const val MSG_KEY = "MESSAGE_KEY"
    }
}

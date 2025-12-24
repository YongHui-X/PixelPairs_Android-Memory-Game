package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.appfiles.androidca.models.Users
import android.content.Intent

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button

    private val users = listOf(
        Users("Tin","test",true),
        Users("CherWah","test",false),
        Users("Michael","test",false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        username = findViewById<EditText>(R.id.login_username)
        password = findViewById<EditText>(R.id.login_password)
        login = findViewById<Button>(R.id.btn_login)

        login.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            auth(username, password)
        }
    }

    private fun auth(username: String, password: String){
        val user = users.find {
            it.username == username && it.password == password
        }
        if (user != null) {
            Toast.makeText(this, "Login successful.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, FetchActivity::class.java)
            intent.putExtra("username", user.username)
            intent.putExtra("isPaid", user.isPaid)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }
}
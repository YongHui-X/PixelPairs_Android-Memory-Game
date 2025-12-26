package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import iss.nus.edu.sg.appfiles.androidca.models.Users
import android.content.Intent
import iss.nus.edu.sg.appfiles.androidca.databinding.ActivityLoginBinding
import iss.nus.edu.sg.appfiles.androidca.fetch.FetchActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val users = listOf(
        Users("Tin","test",true),
        Users("CherWah","test",false),
        Users("Michael","test",false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButtons()
    }

    fun initButtons(){
        binding.btnLogin.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                auth(username, password)
            }
        }
    }

    fun auth(username: String, password: String){
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
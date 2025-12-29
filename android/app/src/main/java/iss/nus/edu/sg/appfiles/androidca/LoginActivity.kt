package iss.nus.edu.sg.appfiles.androidca

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import iss.nus.edu.sg.appfiles.androidca.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButtons()
    }

    private fun initButtons(){
        binding.btnLogin.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter username and password",
                    Toast.LENGTH_SHORT).show()
            } else {
                auth(username, password)
            }
        }
    }

    private fun auth(username: String, password: String){
        Thread {
            try {
                val url = URL("http://10.0.2.2:5119/api/auth/login")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val jsonRequest = JSONObject()
                jsonRequest.put("username", username)
                jsonRequest.put("password", password)

                connection.outputStream.write(jsonRequest.toString().toByteArray())

                val responseCode = connection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val jsonResponse = JSONObject(response)

                    val success = jsonResponse.getBoolean("success")

                    runOnUiThread {
                        if (success) {
                            val responseUsername = jsonResponse.getString("username")
                            val isPaid = jsonResponse.getBoolean("isPaid")

                            Toast.makeText(this, "Login successful.",
                                Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, FetchActivity::class.java)
                            intent.putExtra("username", responseUsername)
                            intent.putExtra("isPaid", isPaid)

                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Invalid username or password",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "Network Error: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}
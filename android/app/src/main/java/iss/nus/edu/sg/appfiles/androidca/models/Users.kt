package iss.nus.edu.sg.appfiles.androidca.models

data class Users(
    val username: String,
    val password: String,
    val isPaid: Boolean = false
)
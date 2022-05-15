package com.example.lab_restaurantes_yuritzy_rodriguez

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val firebaseInstance = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun validateUsers(view: View) {
        val mail = findViewById<EditText>(R.id.editText_mail).text.toString()
        val password = findViewById<EditText>(R.id.editText_password).text.toString()

        firebaseInstance.signInWithEmailAndPassword(mail, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                startActivity(Intent(this, RestaurantsActivity::class.java))
            } else {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            finish()
        }
    }

}
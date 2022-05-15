package com.example.lab_restaurantes_yuritzy_rodriguez

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private val firebaseInstance = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(view: View) {
        val mail = findViewById<TextView>(R.id.editText_register_mail).text.toString()
        val password = findViewById<TextView>(R.id.editText_register_password).text.toString()

        firebaseInstance.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { authResult ->
            if (authResult.isSuccessful) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "El registro falló, intente nuevamente", Toast.LENGTH_LONG).show()
            }
        }
    }

}
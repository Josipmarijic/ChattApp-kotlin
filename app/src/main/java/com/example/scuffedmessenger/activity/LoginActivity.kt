package com.example.scuffedmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.scuffedmessenger.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    //Viewbinding för att hämta mina ui element enklare samt firebase tillbhör
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //instanierar firebase samt viewbinging
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!




        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Logga in knapp
        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            //Toast om man inte skriver i något
            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext, "Email and password are requiered",Toast.LENGTH_SHORT).show()
            //Firebaseauth funktion för att logga in
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            val intent = Intent(this@LoginActivity, UsersActivity::class.java)
                            startActivity(intent)
                        }else{
                            Toast.makeText(applicationContext,"Email or password is invalid",Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
        //Knapp för att komma till signup sidan
        binding.btnSignUp.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}
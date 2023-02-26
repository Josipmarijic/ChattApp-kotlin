package com.example.scuffedmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.scuffedmessenger.databinding.ActivitySignUpBinding
import com.example.scuffedmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRefrence: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        auth = FirebaseAuth.getInstance()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        FirebaseDatabase.getInstance()

        setContentView(binding.root)

        //Logga in knapp som kollar att man har fyllt i allt och sedan skickar till regusterUser funktionen
        binding.btnSignUp.setOnClickListener{
            val userName = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (TextUtils.isEmpty(userName)) {
                Toast.makeText(applicationContext,"Username is required",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext,"Email is required",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext,"Password is required",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(applicationContext,"Please confirm your password is required",Toast.LENGTH_SHORT).show()
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(applicationContext,"Please enter the correct password is required",Toast.LENGTH_SHORT).show()
            }
            registerUser(userName,email,password)
        }

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    //Firebaseauth funktion som läggar till en användar i min firebaseauth samt i min databas
    private fun registerUser(userName:String,email:String,password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseRefrence = FirebaseDatabase.getInstance().getReference("Users").child(userId)


                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",userName)
                    hashMap.put("profileImage","")

                    databaseRefrence.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){

                            binding.etName.setText("")
                            binding.etEmail.setText("")
                            binding.etPassword.setText("")
                            binding.etConfirmPassword.setText("")
                            val intent = Intent(this@SignUpActivity, UsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}
package com.example.scuffedmessenger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import com.bumptech.glide.Glide
import com.example.scuffedmessenger.R
import com.example.scuffedmessenger.databinding.ActivityProfileBinding
import com.example.scuffedmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var backpressed: OnBackPressedDispatcher
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message,Toast.LENGTH_SHORT).show()
            }
            //Ändrar bild och användar namn till den användaren som är inloggad
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
//                binding.etUserName.setText(user!!.userName)

               // if (user.userImage == ""){
                 //   binding.userImage.setImageResource(R.drawable.ic_launcher_background)
                //}else{
                 //   Glide.with(this@ProfileActivity).load(user.userImage).into(binding.userImage)
               // }
            }
        })
        //Tillbaka knapp
        binding.imgBack.setOnClickListener{
            backpressed.onBackPressed()
        }


    }
}
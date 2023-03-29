package com.example.scuffedmessenger.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.scuffedmessenger.R
import com.example.scuffedmessenger.adapter.UserAdapter
import com.example.scuffedmessenger.databinding.ActivityUsersBinding
import com.example.scuffedmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView



class UsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersBinding
    var userList = ArrayList<User>()
    private lateinit var backPressed: OnBackPressedDispatcher
    override fun onCreate(savedInstanceState: Bundle?) {


        binding = ActivityUsersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)





        binding.userRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)


        binding.imgBack.setOnClickListener{
            backPressed.onBackPressed()
        }
       // binding.imgProfile.setOnClickListener{
         //   val intent = Intent(this@UsersActivity, ProfileActivity::class.java)
           // startActivity(intent)
        //}

        getUsersList()







    }

    fun getUsersList(){
        val firebase:FirebaseUser = FirebaseAuth.getInstance().currentUser!!


        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message,Toast.LENGTH_SHORT).show()
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                val currentUser = snapshot.getValue(User::class.java)
              //  if (currentUser != null) {
                  //  if (currentUser.userImage == ""){
                    //    val userImage = findViewById<CircleImageView>(R.id.userImage)
                     //   userImage.setImageResource(R.drawable.ic_launcher_background)
                    //}else{
                      //  Glide.with(this@UsersActivity).load(currentUser.userImage).into(findViewById(R.drawable.ic_launcher_background))
                    //}
                //}

                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user = dataSnapShot.getValue(User::class.java)

                    if (!user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }
                }

                val userAdapter = UserAdapter(this@UsersActivity,userList)

                binding.userRecyclerView.adapter = userAdapter
            }
        })
    }
}
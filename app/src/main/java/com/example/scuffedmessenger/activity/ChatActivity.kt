package com.example.scuffedmessenger.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.scuffedmessenger.R
import com.example.scuffedmessenger.adapter.ChatAdapter
import com.example.scuffedmessenger.databinding.ActivityChatBinding
import com.example.scuffedmessenger.model.Chat
import com.example.scuffedmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    //Firebase samt min viewbinding och onbackpressed.
    var firebaseUser:FirebaseUser? = null
    var refrence:DatabaseReference? = null
    var chatList = ArrayList<Chat>()
    private lateinit var binding: ActivityChatBinding
    private lateinit var backPressed:OnBackPressedDispatcher
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //instansierar min binding så jag enklare kan hitta mina UI element
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        //Hämtar datan som jag passerade från den förra activityn
        var intent = getIntent()
        var userId = intent.getStringExtra("userId")
        var userName = intent.getStringExtra("userName")

        //Instansierar firebase och användaren som är inloggad och hämtar datan för den usern i databasen
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refrence = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        //Gå tillbaka knapp
        binding.imgBack.setOnClickListener{
            backPressed.onBackPressed()
        }

        refrence!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            //Hämtar data från firebase realtime database snapshot och displayar det i min ui element
            override fun onDataChange(snapshot: DataSnapshot) {


                val user = snapshot.getValue(User::class.java)
                binding.tvUserName.text = user!!.userName
              //  if (user.userImage == ""){
                //    binding.imgProfile.setImageResource(R.drawable.ic_launcher_foreground)
                //}else{
                  //  Glide.with(this@ChatActivity).load(user.userImage).into(binding.imgProfile)
                //}
            }
        })

        //skicka meddelande, om man trycker på knappen utan ett meddelande så får man toast msg annars skickas meddelandet, userid
        // och mottagerens userid till sendmessege fuktionen
        binding.sendMsg.setOnClickListener{
            var messege:String = binding.msgText.text.toString()

            if (messege.isEmpty()){
                Toast.makeText(applicationContext,"Enter a message",Toast.LENGTH_SHORT).show()
                binding.msgText.setText("")
            }else{
                sendMessage(firebaseUser!!.uid,userId,messege)
                binding.msgText.setText("")
            }
        }

        readMesseage(firebaseUser!!.uid, userId)
    }


    //Lägger till mottagarens id avsändarens id och meddelandet till en hashmap som senare sparas i firebase databasen
    private fun sendMessage(senderId:String, reciverId:String, message:String){
        var refrence:DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap:HashMap<String,String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("reciverId", reciverId)
        hashMap.put("messege", message)

        refrence!!.child("Chat").push().setValue(hashMap)
    }

    fun readMesseage(senderId: String,reciverId: String){
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            //Callback funktion som hämtar snapshot från databasen och visar dom om dom matchar avsändar id och mottagar id så att man kan se meddelanden
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.reciverId.equals(reciverId) ||
                        chat!!.senderId.equals(reciverId) && chat!!.reciverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }

                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)
                binding.chatRecyclerView.adapter = chatAdapter
            }




        })


    }

}
package com.example.scuffedmessenger.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.scuffedmessenger.R
import com.example.scuffedmessenger.activity.ChatActivity
import com.example.scuffedmessenger.model.Chat
import com.example.scuffedmessenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdapter(private val context: Context, private val chatList:ArrayList<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSEGE_TYPE_LEFT = 0
    private val MESSEGE_TYPE_RIGHT = 1
    var firebaseUser:FirebaseUser? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSEGE_TYPE_RIGHT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
                return ViewHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            return ViewHolder(view)

        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtUserName.text = chat.message


    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val txtUserName:TextView = view.findViewById(R.id.tvMessage)
       // val imgUser:CircleImageView = view.findViewById(R.id.userImage)

    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (chatList[position].senderId == firebaseUser!!.uid){
            return MESSEGE_TYPE_RIGHT
        }else{
            return MESSEGE_TYPE_LEFT
        }
    }
}
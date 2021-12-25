package com.example.musicapp.Adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Message
import kotlinx.android.synthetic.main.chat_into_notification.view.*
import kotlinx.android.synthetic.main.friend_msg_row.view.*
import kotlinx.android.synthetic.main.msg_row.view.*

class ChatBoxAdapter(val context: Activity, val chatList : ArrayList<Message>): RecyclerView.Adapter<ChatBoxAdapter.MyViewHolder>() {
    val CHAT_MINE = 0
    val CHAT_PARTNER = 1
    val USER_JOIN = 2
    val USER_LEAVE = 3



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {

        val id=  context.intent.getIntExtra("Uid",0)
        Log.d("chatlist size",chatList.size.toString())
        var view : View? = null
        when(viewType){

            0 ->{

                view = LayoutInflater.from(context).inflate(R.layout.msg_row,parent,false)
                Log.d("user inflating","viewType : ${viewType}")
            }

            1 ->
            {
                view = LayoutInflater.from(context).inflate(R.layout.friend_msg_row,parent,false)
                Log.d("partner inflating","viewType : ${viewType}")
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.chat_into_notification,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.chat_into_notification,parent,false)
                Log.d("someone in or out","viewType : ${viewType}")
            }
        }

        return MyViewHolder(view!!)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val messageData  = chatList[position]
        val userName = messageData.userName
        val content = messageData.message
        val viewType = messageData.viewType

        when(viewType) {
            CHAT_MINE -> {
                holder.message.text = content
            }
            CHAT_PARTNER ->{
                holder.UserName.text = userName
                holder.messageFriend.text = content
            }
            USER_JOIN -> {
                val text = "${userName} has created the Group"
                holder.text.setText(text)
            }
            USER_LEAVE -> {
                val text = "${userName} has leaved the room"
                holder.text.setText(text)
            }
        }
    }

    override fun getItemCount(): Int {
        return chatList.size


    }
    override fun getItemViewType(position: Int): Int {
        return chatList[position].viewType
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message = itemView.message2
        var messageFriend = itemView.message
        var UserName = itemView.username
        var text = itemView.text
    }
}

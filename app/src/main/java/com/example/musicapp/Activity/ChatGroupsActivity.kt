package com.example.musicapp.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.Adapter.ChatBoxAdapter
import com.example.musicapp.R
import com.example.musicapp.SocketCreate
import com.example.musicapp.model.Message
import com.example.musicapp.model.MessageType
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_chat_box.*
import kotlinx.android.synthetic.main.activity_chat_groups.*
import kotlinx.android.synthetic.main.activity_chat_groups.ed_message
import kotlinx.android.synthetic.main.activity_chat_groups.img_send
import kotlinx.android.synthetic.main.activity_chat_groups.leave
import kotlinx.android.synthetic.main.activity_chat_groups.recyclerView
import org.json.JSONException
import org.json.JSONObject

class ChatGroupsActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    val chatList: ArrayList<Message> = arrayListOf()
    lateinit var chatBoxAdapter: ChatBoxAdapter
    private lateinit var myName:String
    private lateinit var userName:String
    private var myId:Int=0
    private var userId:Int=0
//    lateinit var group_ids:String
    private var isTyping:Boolean=false
    var share: SharedPreferences? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_groups)

        val groupName = intent.getStringExtra("group_name")
       val authorName = intent.getStringExtra("author_name")
        group_name_chat.text=groupName


        app = application as SocketCreate
        mSocket = app.getSocket()



        img_send.setOnClickListener(this)
        leave.setOnClickListener(this)

        chatBoxAdapter = ChatBoxAdapter(this, chatList);
        recyclerView.adapter = chatBoxAdapter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        share = getSharedPreferences("login", Context.MODE_PRIVATE)



        myId = share!!.getInt("Uid",-1)
        myName = share!!.getString("Uname","")!!
//        group_ids =intent!!.getStringExtra("group_ids")!!
        text.setText("$myName has created the Group")
    mSocket!!.on("message", onNewMessageGroup)

        mSocket!!.connect()

        img_send.setOnClickListener {
           sendMessage()
        }
    }






    var onNewMessageGroup= Emitter.Listener { args ->
        runOnUiThread{
            try{

                val message = args[0] as JSONObject
                userName =message.getString("user_name")
                val msg =message.getString("message")
                Log.e("message",message.toString())

//                val group_ids =message.getString("group_ids")
//                val userId=message.getInt("source_id")
//               if(group_ids.contains(userId.toString())){
                val chat = Message(userName, msg,  MessageType.CHAT_PARTNER.index)
                addItemToRecyclerView(chat)
//           }
            }catch (e: Exception) {
                Log.i("TAG", e.toString())

            }
        }
    }




    private fun sendMessage() {
        var data= JSONObject()
        data.put("user_name", myName)
        data.put("message", ed_message.text.toString())
        data.put("source_id", myId)
//        data.put("group_ids", group_ids)
        Log.e("data",data.toString())
        mSocket!!.emit("message", data)
        val message = Message("", ed_message.text.toString() , MessageType.CHAT_MINE.index)
        addItemToRecyclerView(message)

    }





    private fun addItemToRecyclerView(message: Message) {
        Log.e("message",message.toString())
        runOnUiThread {
            chatList.add(message)
            chatBoxAdapter.notifyItemInserted(chatList.size)
            ed_message.setText("")
            recyclerView.scrollToPosition(chatList.size - 1) //move focus on last message
        }
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
           R.id.img_send -> sendMessage()
            R.id.leave ->  {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
            }
        }
    }
}

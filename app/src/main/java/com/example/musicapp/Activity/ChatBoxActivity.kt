package com.example.musicapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.Adapter.ChatBoxAdapter
import com.example.musicapp.DbManager
import com.example.musicapp.R
import com.example.musicapp.SocketCreate
import com.example.musicapp.model.Message
import com.example.musicapp.model.MessageType
import com.example.musicapp.model.Users
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_chat_box.*
import kotlinx.android.synthetic.main.activity_chat_box.ed_message
import kotlinx.android.synthetic.main.activity_chat_box.img_send
import kotlinx.android.synthetic.main.activity_chat_box.leave
import kotlinx.android.synthetic.main.activity_chat_box.recyclerView
import kotlinx.android.synthetic.main.activity_chat_groups.*
import org.json.JSONException

import org.json.JSONObject


class ChatBoxActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    val chatList: ArrayList<Message> = arrayListOf()
    lateinit var chatBoxAdapter: ChatBoxAdapter
    private lateinit var myName:String
    private lateinit var userName:String
    private var id:Int=0
    private var userId:Int=0
    private var isTyping:Boolean=false
    private var startTyping = false
    private var time = 2
    @SuppressLint("HandlerLeak")
    var handler2: Handler = object : Handler() {
        override fun handleMessage(msg: android.os.Message?) {
            super.handleMessage(msg!!)
            Log.i("msg", "handleMessage: typing stopped $startTyping")
            if (time == 0) {
                title = "SocketIO"
                Log.i("msg", "handleMessage: typing stopped time is $time")
                startTyping = false
                time = 2
            }
        }
    }






    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_box)
        app = application as SocketCreate
        mSocket = app.getSocket()



        img_send.setOnClickListener(this)
        leave.setOnClickListener(this)

        chatBoxAdapter = ChatBoxAdapter(this, chatList);
        recyclerView.adapter = chatBoxAdapter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

//userId
        myName = intent!!.getStringExtra("name")!!
        id = intent!!.getIntExtra("source_id", 0)
        userId = intent!!.getIntExtra("userId", 0)
        userName= intent.getStringExtra("userName")!!
        user_name.text=userName

        mSocket!!.on("on typing",onType)
        mSocket!!.on("message", onNewMessage)





        mSocket!!.connect()

        img_send.setOnClickListener {
            sendMessage()
        }
        onTypeButtonEnable()
    }




    fun onTypeButtonEnable() {
        ed_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val onTyping = JSONObject()
                try {
                    onTyping.put("typing", true)
                    onTyping.put("username", myName)
                    onTyping.put("uniqueId", id)
                    mSocket!!.emit("on typing", onTyping)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (charSequence.toString().trim { it <= ' ' }.isNotEmpty()) {
                    img_send.setEnabled(true)
                } else {
                    img_send.setEnabled(false)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    @SuppressLint("SetTextI18n")
    var onType=Emitter.Listener { args ->
        runOnUiThread {
            var data = args[0] as JSONObject
            Log.i("msg", "run: " + args[0]);
            try {
                var typingOrNot = data.getBoolean("typing")
                var userName = data.getString("username") + " is Typing......"
                var id = data.getInt("uniqueId")

                if (id.equals(userId)) {
                    typingOrNot = false;
                    typing.text="Online"

                } else {
                    setTitle(userName)
                }

                if (typingOrNot) {

                    if (!startTyping) {
                        startTyping = true
                        typing.text="is typing.."
                        runOnUiThread {
                            while (time > 0) {
                                synchronized(this) {
                                    try {
                                        Thread.sleep(1000)
                                    } catch (e: InterruptedException) {
                                        e.printStackTrace();
                                    }
                                    time--
                                }
                                handler2.sendEmptyMessage(0);
                            }
                        }
                    }else {
                        time = 2
                        typing.text="online"

                    }
                }
            }
            catch (e: JSONException) {
                e.printStackTrace();
            }

        }
    }


    var onNewMessage=Emitter.Listener { args ->
        runOnUiThread{
            try{
                val message = args[0] as JSONObject
                Log.e("user",id.toString()+"des: "+message.getInt("dest_id"))
                if (id == message.getInt("dest_id")) {
          //     val nameF =message.getString("user_name")
                    val msg =message.getString("message")
                    val chat = Message(userName, msg,  MessageType.CHAT_PARTNER.index)
                    addItemToRecyclerView(chat)

          }
            }catch (e: Exception) {
                Log.i("TAG", e.toString())

            }
        }
    }






    private fun sendMessage() {
        var data=JSONObject()
        // from sign in
        data.put("user_name", myName)
        data.put("friend_name", userName)

        data.put("message", ed_message.text.toString())
        //user_id from login
        data.put("source_id", id)
        //id from users screen that click on it
        data.put("dest_id", userId)
        mSocket!!.emit("message", data)
        val message = Message("", ed_message.text.toString() ,MessageType.CHAT_MINE.index)
        addItemToRecyclerView(message)

    }
    
    
    
    

    private fun addItemToRecyclerView(message: Message) {
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


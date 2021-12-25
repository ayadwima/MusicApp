package com.example.musicapp.Activity


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.musicapp.DbManager
import com.example.musicapp.Fragment.Groups_view_Fragment
import com.example.musicapp.Fragment.OnlineUserFragment
import com.example.musicapp.R
import com.example.musicapp.SocketCreate
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject


class HomeActivity : AppCompatActivity() {

    lateinit var name: String
    var uId: Int = 0
    var share: SharedPreferences? = null
    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    var isActive:Boolean=false
    var isCheck:Boolean = false
    var db: DbManager? = null



    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chat_private -> {
                    replaceFragment(OnlineUserFragment())
                    return@OnNavigationItemSelectedListener true

                }
                R.id.chat_group -> {
                    replaceFragment(Groups_view_Fragment())
                    return@OnNavigationItemSelectedListener true
                }

            }
            false

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        setSupportActionBar(toolbar)
        nav_view.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        nav_view.selectedItemId =R.id.chat_private

        db = DbManager(this)

        share = getSharedPreferences("login", Context.MODE_PRIVATE)

        uId = share!!.getInt("Uid",-1)
        name = share!!.getString("Uname","")!!

        app = application as SocketCreate
        mSocket = app.getSocket()




        isActive=true

        mSocket!!.on(Socket.EVENT_CONNECT_ERROR) {
          runOnUiThread {
                Log.e("socket", "EVENT_CONNECT_ERROR: ")
            }
        }
        mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, Emitter.Listener {
            runOnUiThread {
                Log.e("socket", "EVENT_CONNECT_TIMEOUT: ")

            }
        })
        mSocket!!.on(
            Socket.EVENT_CONNECT
        ) {

            Log.e("socket", "Socket Connected!")


        }
        mSocket!!.on(Socket.EVENT_DISCONNECT, Emitter.Listener {
           runOnUiThread {
                Log.e("socket", "Socket onDisconnect!")

            }
        })
        mSocket!!.connect()
        joinUser()


    }



    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.mainContainer,
            fragment
        ) .addToBackStack(null).commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            nav_view.visibility=View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }

    private fun joinUser() {

        val message = JSONObject()
        message.put("source_id", uId)
        message.put("name", name)
        message.put("isActive", isActive)
        mSocket!!.emit("join", message)


    }
}

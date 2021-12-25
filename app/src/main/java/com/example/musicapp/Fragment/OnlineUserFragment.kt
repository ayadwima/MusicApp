package com.example.musicapp.Fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.Activity.ChatBoxActivity
import com.example.musicapp.Adapter.OnlineUserAdapter
import com.example.musicapp.R
import com.example.musicapp.SocketCreate
import com.example.musicapp.model.Users
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_online_user.view.*
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 */
class OnlineUserFragment : Fragment() , OnlineUserAdapter.OnClickListener{

    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    lateinit var uName: String
    var uId: Int = 0
    private val users = ArrayList<Users>()

    var share: SharedPreferences? = null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        // Inflate the layout for this fragment

        val root =inflater.inflate(R.layout.fragment_online_user, container, false)

        share = activity!!.getSharedPreferences("login", Context.MODE_PRIVATE)

        uId = share!!.getInt("Uid",-1)
        uName = share!!.getString("Uname","")!!

        app = activity!!.application as SocketCreate
        mSocket = app.getSocket()
        activity!!.toolbar.title = "Online Users"


        root.create_group.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().replace(
                R.id.mainContainer,
                CreateGroupFragment()
            ) .addToBackStack(null).commit()

           activity!!.nav_view.visibility =View.GONE
        }



        mSocket!!.emit("connect1")

        mSocket!!.on("connect1", Emitter.Listener { args ->
            users.clear()
            if (args.isNotEmpty()){
                val user = args[0] as JSONArray
                Log.e("socket", "getOnlineUsers Successfully 1 ")
                for (i in 0 until user.length()){
                    val joinId:Int =user.getJSONObject(i).getInt("source_id")
                    val joinName: String=user.getJSONObject(i).getString("name")
                    if(joinId!=uId) {
                        users.add(Users(joinId, joinName,"",false))
                    }
                }
                activity!!. runOnUiThread {
                    root!!.online_recycler.layoutManager = LinearLayoutManager(activity!!)
                  root!!.online_recycler.setHasFixedSize(true)
                    val adapter = OnlineUserAdapter(activity!!, users , this)
                    root!!.online_recycler.adapter = adapter
                }
            }

        })



        mSocket!!.on("join", Emitter.Listener { args ->
            users.clear()
            val user = args[0] as JSONArray
            Log.e("socket", "Listened Successfully !")
            for (i in 0 until user.length()) {
                val joinId: Int = user.getJSONObject(i).getInt("source_id")
                val joinName: String = user.getJSONObject(i).getString("name")
                if(joinId!=uId) {
                    users.add(Users(joinId, joinName,"",false))
                }
            }
           activity!!.runOnUiThread {
                root!!. online_recycler.layoutManager = LinearLayoutManager(activity!!)
                root!!. online_recycler.setHasFixedSize(true)
                val adapter = OnlineUserAdapter(activity!!, users , this)
                root!!.online_recycler.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })




        return root!!
    }





    override fun OnClick(i: Int) {
        val intent = Intent(activity!!, ChatBoxActivity::class.java)
        intent.putExtra("userName", users[i].name)
        intent.putExtra("userId", users[i].id)
        intent.putExtra("name", uName)
        intent.putExtra("source_id", uId)
        startActivity(intent)


    }

}

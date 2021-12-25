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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.musicapp.Adapter.GroupCreateAdapter
import com.example.musicapp.Activity.ChatGroupsActivity

import com.example.musicapp.R
import com.example.musicapp.SocketCreate
import com.example.musicapp.model.Users
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_chat_groups.*

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_create_group.*
import kotlinx.android.synthetic.main.fragment_create_group.view.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class CreateGroupFragment : Fragment() {
    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    var data : ArrayList<Users>?= null

    var uId: Int = 0
    lateinit var uName: String

    private val users = ArrayList<Users>()

    var share: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
   val root =  inflater.inflate(R.layout.fragment_create_group, container, false)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        data = ArrayList()
        var checkedId = ArrayList<Int>()

        app = activity!!.application as SocketCreate
        mSocket = app.getSocket()
        share = activity!!.getSharedPreferences("login", Context.MODE_PRIVATE)

        uId = share!!.getInt("Uid",-1)
        uName = share!!.getString("Uname","")!!

        app = activity!!.application as SocketCreate
        mSocket = app.getSocket()
        activity!!.nav_view.visibility =View.GONE



        activity!!.toolbar.title = "Create Group"
        activity!!.toolbar.setNavigationOnClickListener {
            activity!!.onBackPressed()
        }




        var adapter = GroupCreateAdapter(activity!!, users)
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
                activity!!.runOnUiThread {
                    root!!.users_group_recycler.layoutManager = LinearLayoutManager(activity!!)
                    root.users_group_recycler.setHasFixedSize(true)
                    adapter = GroupCreateAdapter(activity!!, users )
                    root.users_group_recycler.adapter = adapter
                }
            }

        })




 val group_id=(Math.random()*1000).toInt()
        root.done_group.setOnClickListener {
            var counter=0
            checkedId.add(uId)
            for (i in adapter.data!!){
                if (i.isCheck){
                    checkedId.add(i.id)
                    counter++
                }
            }
            if (Group_name.text!!.isNotEmpty()&&checkedId.isNotEmpty()){
                val bundle = Bundle()
                bundle.putString("usersId",checkedId.toString())
                bundle.putString("group_name",Group_name.text.toString())
                bundle.putString("groupId",group_id.toString())
                val group=JSONObject()
                group.put("groupId",group_id)
                group.put("group_name",Group_name.text.toString())
                mSocket!!.emit("createGroup",group,checkedId)
                val intent = Intent(activity!!, ChatGroupsActivity::class.java)
                intent.putExtra("group_name",Group_name.text.toString())
                 intent.putExtra("author_name",uName)


                startActivity(intent)

            }else{
                Toast.makeText(activity!!,"please choose users and enter group name",Toast.LENGTH_LONG).show()
            }

        }




        mSocket!!.on("join", Emitter.Listener { args ->
            users.clear()
            val user = args[0] as JSONArray
            Log.e("socket", "Listened Successfully !")
            for (i in 0 until user.length()) {
                val connectedId: Int = user.getJSONObject(i).getInt("source_id")
                val connectedName: String = user.getJSONObject(i).getString("name")
                if(connectedId!=uId) {
                    users.add(Users(connectedId, connectedName,"",false))
                }
            }
            activity!!.runOnUiThread {
                root!!.users_group_recycler.layoutManager = LinearLayoutManager(activity!!)
                root.users_group_recycler.setHasFixedSize(true)
                val adapter = GroupCreateAdapter(activity!!, users )
                root.users_group_recycler.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })





        return  root
    }




}

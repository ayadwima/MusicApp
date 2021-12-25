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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.Adapter.GroupsAdapter
import com.example.musicapp.Activity.ChatGroupsActivity

import com.example.musicapp.R
import com.example.musicapp.SocketCreate
import com.example.musicapp.model.Groups
import com.example.musicapp.model.Users
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_group_.view.*
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class Groups_view_Fragment : Fragment() , GroupsAdapter.OnClickListener {
    lateinit var app: SocketCreate
    private var mSocket: Socket? = null
    var data: ArrayList<Groups>? = null
    var users_group: String? = null
    var share: SharedPreferences? = null
    private val users = ArrayList<Users>()
    lateinit var uName: String

    var uId: Int = 0
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_group_, container, false)
        share = activity!!.getSharedPreferences("login", Context.MODE_PRIVATE)
        uName = share!!.getString("Uname","")!!
        data = ArrayList()
        app = activity!!.application as SocketCreate
        mSocket = app.getSocket()



        activity!!.toolbar.title = "Groups"


        mSocket!!.on("createGroup", Emitter.Listener { args ->
            val group = args[0] as JSONObject
            users_group = args[1].toString()
            uId = share!!.getInt("Uid",0)
            val group_id = group.getInt("groupId")
            val group_name = group.getString("group_name")
                data!!.add(Groups(group_id, group_name, ""))


            Log.e("mm",users_group!!)
            Log.e("mm",group.toString())
            Log.e("mm",uId.toString())

            activity!!.runOnUiThread {
                root.group_recycler.layoutManager = LinearLayoutManager(activity!!)
                root.group_recycler.setHasFixedSize(true)
                val adapter = GroupsAdapter(activity!!, data!!,this)
                root.group_recycler.adapter = adapter
//                 adapter.data.clear()
                    adapter.data.addAll(data!!)
                adapter.notifyDataSetChanged()
            }
        })




        return root


    }

    override fun OnClick(i: Int) {
        val intent = Intent(activity!!, ChatGroupsActivity::class.java)
        intent.putExtra("group_name", data?.get(i)!!.GroupName)
        intent.putExtra("group_ids", users_group!!)
        startActivity(intent)
    }
}

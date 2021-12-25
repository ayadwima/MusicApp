package com.example.musicapp.Adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Users
import kotlinx.android.synthetic.main.online_user_row.view.*

class OnlineUserAdapter(var activity: FragmentActivity, var data: ArrayList<Users>) :
    RecyclerView.Adapter<OnlineUserAdapter.MyViewHolder>() {

    var onclick:OnClickListener? = null

    constructor(activity: FragmentActivity, data: ArrayList<Users>, onclick: OnClickListener):this(activity, data) {
        this.onclick = onclick
    }


    var id = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.online_user_row, parent, false)
        return MyViewHolder(itemView)



    }



    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]

        id = data.id
        holder.id=id

        holder.tvUserName.text =data.name.toString()

        holder.tvUserName.setOnClickListener {
            onclick!!.OnClick(holder.adapterPosition)
        }

    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName = itemView.tv_user_name
        var id = itemView.id
    }


    interface OnClickListener {
        fun OnClick(i: Int )
    }

}




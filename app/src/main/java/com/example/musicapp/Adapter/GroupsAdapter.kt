package com.example.musicapp.Adapter



import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Groups
import kotlinx.android.synthetic.main.groups_row.view.*


class GroupsAdapter (var activity: FragmentActivity, var data: ArrayList<Groups>) :
    RecyclerView.Adapter<GroupsAdapter.MyViewHolder>() {

    var onclick:OnClickListener? = null

    constructor(activity: FragmentActivity, data: ArrayList<Groups>, onclick: OnClickListener):this(activity, data) {
       this.onclick = onclick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.groups_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = data[position]
        holder.groupName.text = data.GroupName.toString()

        holder.groupName.setOnClickListener {
            onclick!!.OnClick(holder.adapterPosition)
        }

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName = itemView.tv_group_name

    }



    interface OnClickListener {
        fun OnClick(i: Int )
    }

}
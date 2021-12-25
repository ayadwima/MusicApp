package com.example.musicapp.Adapter



import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.R
import com.example.musicapp.model.Users
import kotlinx.android.synthetic.main.group_create_row.view.*
import kotlinx.android.synthetic.main.online_user_row.view.tv_user_name


class GroupCreateAdapter (var activity: Activity, var data: ArrayList<Users>) :
    RecyclerView.Adapter<GroupCreateAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.group_create_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size    }

    override fun onBindViewHolder(holder:MyViewHolder, position: Int) {

        holder.tvUserName.text = data[position].name.toString()
        holder.checkBox.setOnCheckedChangeListener(){ _, isChecked ->
            data[position].isCheck = isChecked

        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvUserName = itemView.tv_user_name
        var id = itemView.id
        val checkBox = itemView.checkbox
    }

}
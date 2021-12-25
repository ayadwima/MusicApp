package com.example.musicapp.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.DbManager
import com.example.musicapp.R
import com.example.musicapp.SocketCreate
import com.example.musicapp.model.JoinUser
import com.example.musicapp.model.Users
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {
    lateinit var app: SocketCreate
    var sharedPref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var db: DbManager? = null
    var users: ArrayList<JoinUser>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        editor = sharedPref!!.edit()
        db = DbManager(this)
        users = db!!.getAllUsers()

        fun validate(): Boolean {
            for (i in 0 until users!!.size) {
                if (ed_user_name.text.toString().equals(users!![i].name) && ed_password.text.toString().equals((users!![i].password))) {
                    editor!!.putInt("Uid", users!![i].id).commit()
                    editor!!.putString("Uname", users!![i].name).commit()
                    editor!!.commit()
                    return true
                }
            }
            ed_password.error = "Password error"
            return false
        }

        db = DbManager(this)


        btn_sign_in.setOnClickListener {

            users = db!!.getAllUsers()
            if (validate()) {
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
                finish()


            }

        }

    }



}



package com.example.musicapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.musicapp.DbManager
import com.example.musicapp.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    var db: DbManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        db = DbManager(this)
        btn_sign_up.setOnClickListener {

            val userName = ed_user_name.text.toString()
            val password = ed_password.text.toString()



            if (userName.isEmpty() && password.isEmpty() ) {
                Toast.makeText(this, "login Faild", Toast.LENGTH_SHORT).show()

            } else {
                var id = (Math.random()*1000).toInt()
                if  (db!!.insertUsers(id,userName, password))
                    Log.e("mm",  "Sucess")

                val i = Intent(applicationContext, SignInActivity::class.java)
                startActivity(i)
            }


        }
    }
}


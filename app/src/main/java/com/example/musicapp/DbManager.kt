package com.example.musicapp

import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.musicapp.model.JoinUser
import com.example.musicapp.model.Users

class DbManager(activity: Activity): SQLiteOpenHelper(activity, DATABASE_NAME, null, DATABASE_VERSION)  {


    private val db: SQLiteDatabase = this.writableDatabase


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(JoinUser.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("Drop table if exists ${JoinUser.TABLE_NAME}")
        onCreate(db)

    }


    fun insertUsers(id:Int,name: String, password: String): Boolean {
        val cv = ContentValues()
        cv.put(JoinUser.COL_ID,id)
        cv.put(JoinUser.name, name)
        cv.put(JoinUser.COL_PASSWORD, password)


        return db.insert(JoinUser.TABLE_NAME, null, cv) > 0

    }

    fun getAllUsers(): ArrayList<JoinUser> {
        val users = ArrayList<JoinUser>()
        val c =
            db.rawQuery("select * from ${JoinUser.TABLE_NAME} ", null)
        c.moveToFirst()
        while (!c.isAfterLast) {
            val user = JoinUser(
                c.getInt(0),
                c.getString(1),
                c.getString(2)

                    )
            users.add(user)
            c.moveToNext()
        }
        c.close()
        return users!!

    }

    companion object {
        const val DATABASE_NAME = "Chat"
        const val DATABASE_VERSION = 17

    }


}
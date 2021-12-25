package com.example.musicapp.model


import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class JoinUser(var id: Int, var name: String?, var password:String?) :
    Parcelable {


    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString()

           ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(id)
        dest.writeString(name)
        dest.writeString(password)




    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JoinUser> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): JoinUser {
            return JoinUser(parcel)
        }

        override fun newArray(size: Int): Array<JoinUser?> {
            return arrayOfNulls(size)
        }

        val TABLE_NAME = "Users"
        val COL_ID = "id"
        val name = "name"
        val COL_PASSWORD = "password"
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME"+
                " ( $COL_ID INTEGER ,"+
                " $name TEXT,"+
                " $COL_PASSWORD TEXT);"

    }
}

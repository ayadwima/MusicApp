package com.example.musicapp.model


import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Users(var id: Int, var name: String?,var password:String?,var isCheck:Boolean) :
    Parcelable {


    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readBoolean()
           ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeInt(id)
        dest.writeString(name)
        dest.writeString(password)
            dest.writeBoolean(isCheck)



    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
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

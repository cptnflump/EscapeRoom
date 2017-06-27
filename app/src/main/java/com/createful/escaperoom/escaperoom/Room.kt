package com.createful.escaperoom.escaperoom

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity

class Room constructor(val roomName: String = "Room", val givenTime: String = "01:00",
                       val roomCode: Array<String> = arrayOf("aaaa","aaaa","aaaa","aaaa"))
    : AppCompatActivity(), Parcelable {

    var id: String? = null
    var noOfCodes: Int = 0

    init {
        id = (roomName.substring(0,1) + givenTime.substring(0,1) + givenTime.substring(3,4))
        noOfCodes = roomCode.count()
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArray())

    //toString override
    override fun toString() : String = roomName

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(roomName)
        parcel.writeString(givenTime)
        parcel.writeStringArray(roomCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}

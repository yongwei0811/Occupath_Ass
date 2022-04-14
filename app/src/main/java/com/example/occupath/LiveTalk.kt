package com.example.occupath

import android.os.Parcel
import android.os.Parcelable

data class LiveTalk(
    val email : String?= null,
    val name : String?= null,
    val talkTitle : String? =null,
    val talkDesc : String?=null,
    val topic : String?=null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(talkTitle)
        parcel.writeString(talkDesc)
        parcel.writeString(topic)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LiveTalk> {
        override fun createFromParcel(parcel: Parcel): LiveTalk {
            return LiveTalk(parcel)
        }

        override fun newArray(size: Int): Array<LiveTalk?> {
            return arrayOfNulls(size)
        }
    }
}
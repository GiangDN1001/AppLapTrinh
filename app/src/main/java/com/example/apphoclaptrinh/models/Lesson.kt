package com.example.apphoclaptrinh.models

import android.os.Parcel
import android.os.Parcelable

data class Lesson(
    val id: Int,
    val title: String,
    val duration: String,
    val description: String,
    val exercises: List<Exercise>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(Exercise.CREATOR) ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(duration)
        parcel.writeString(description)
        parcel.writeTypedList(exercises)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Lesson> {
        override fun createFromParcel(parcel: Parcel): Lesson {
            return Lesson(parcel)
        }

        override fun newArray(size: Int): Array<Lesson?> {
            return arrayOfNulls(size)
        }
    }
}

package com.example.apphoclaptrinh.models


import android.os.Parcel
import android.os.Parcelable

data class ExerciseOption(
    val id: Int,
    val text: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(text)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ExerciseOption> {
        override fun createFromParcel(parcel: Parcel): ExerciseOption {
            return ExerciseOption(parcel)
        }

        override fun newArray(size: Int): Array<ExerciseOption?> {
            return arrayOfNulls(size)
        }
    }
}


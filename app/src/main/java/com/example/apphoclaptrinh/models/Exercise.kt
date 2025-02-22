package com.example.apphoclaptrinh.models

import android.os.Parcel
import android.os.Parcelable

data class Exercise(
    val exercise_id: Int,
    val title: String,
    val question: String,
    val options: List<ExerciseOption>,
    val correct_answer: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(ExerciseOption.CREATOR) ?: emptyList(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(exercise_id)
        parcel.writeString(title)
        parcel.writeString(question)
        parcel.writeTypedList(options)
        parcel.writeString(correct_answer)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Exercise> {
        override fun createFromParcel(parcel: Parcel): Exercise {
            return Exercise(parcel)
        }

        override fun newArray(size: Int): Array<Exercise?> {
            return arrayOfNulls(size)
        }
    }
}



package com.development.task.mymapp.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Links (

	val self : String?,
	val html : String?,
	val download : String?
):Parcelable
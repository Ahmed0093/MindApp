package com.development.task.mymapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Categories (

	val id : Int,
	val title : String,
	val photo_count : Int,
	val links : Links
):Parcelable
package com.development.task.mymapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.development.task.mymapp.Constants.SELECTED_ITEM
import com.development.task.mymapp.model.ResultRespone
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {
    private var selectedItem: ResultRespone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        getIntentData()

    }


    private fun getIntentData() {
        if (!intent.hasExtra(SELECTED_ITEM)) {
            Log.d("INTENT", "getIntentData: could not find selected article")
            finish()
            return
        }

        selectedItem = intent.getParcelableExtra(SELECTED_ITEM) as ResultRespone?
        tvTitleDetails.text = selectedItem?.user?.name
        tvDescription.text = selectedItem?.likes.toString()

    }

}

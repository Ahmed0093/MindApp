package com.development.task.mymapp.providers

import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

/**
 * Created by Ahmed Abdullah on 9/28/2019.
 */
object ProvideContext {

    private lateinit var context: WeakReference<Context>

    fun withContext(applicationContext: Context) {
        context = WeakReference(applicationContext)
    }

    fun getContext() = context.get()!!
}
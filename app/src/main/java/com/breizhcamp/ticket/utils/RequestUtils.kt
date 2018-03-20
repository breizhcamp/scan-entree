package com.breizhcamp.ticket.utils

import android.content.Context;
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


object RequestUtils {

    private val TAG: String = RequestUtils.javaClass.name

    private var mRequestQueue: RequestQueue? =  null


    init {
        Log.d(TAG, "init request queue")
    }

    fun fetchRequestQueue(context:Context):RequestQueue? {
        Log.d(TAG, "fetchRequestQueue")

        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context)
        }

        return mRequestQueue
    }

    fun <T> addToRequestQueue(req:Request<T>){
        mRequestQueue?.add(req)
    }

}

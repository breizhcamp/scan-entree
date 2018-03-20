package com.breizhcamp.ticket.additions

import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Response
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonRequest


import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.UnsupportedEncodingException


class CustomRequest : JsonRequest<JSONArray> {

    constructor(method: Int, url: String, jsonRequest: JSONObject?,
                listener: Listener<JSONArray>, errorListener: ErrorListener) : super(method, url, jsonRequest?.toString(), listener,
            errorListener)

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONArray> {

        return try {

            val jsonString = String(response.data,
                    charset(HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)))
            Response.success(JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {
            Response.error(ParseError(e))
        } catch (je: JSONException) {
            Response.error(ParseError(je))
        }

    }
}

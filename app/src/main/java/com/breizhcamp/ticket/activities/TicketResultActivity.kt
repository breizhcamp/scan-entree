package com.breizhcamp.ticket.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.breizhcamp.ticket.utils.checkinDay
import com.breizhcamp.ticket.utils.daysOfWeek

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import org.json.JSONObject
import org.json.JSONArray

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.breizhcamp.ticket.AppConfig
import com.breizhcamp.ticket.R
import com.breizhcamp.ticket.additions.CustomRequest
import com.breizhcamp.ticket.model.Ticket
import com.breizhcamp.ticket.utils.RequestUtils
import kotlinx.android.synthetic.main.content_ticket_details.*

class TicketResultActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_result)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val barcode = intent.getStringExtra(AppConfig.CODE)

        // close the activity in case of empty barcode
        if (TextUtils.isEmpty(barcode)) {
            Toast.makeText(applicationContext, "Barcode is empty!", Toast.LENGTH_LONG).show()
            finish()
        }

        findViewById<View>(R.id.btn_next).setOnClickListener { this.onBackPressed() }

        // search the barcode
        searchBarcode(barcode)
    }


    /**
     * Retrieve ticket info from barcode by calling POST request
     */
    private fun searchBarcode(barcode: String) {

        val params = JSONObject()
        params.put("s", barcode)

        val method = Request.Method.POST

        val preferences = getSharedPreferences(AppConfig.SHARE_PREFERENCIES_NAME, Context.MODE_PRIVATE)
        val url = preferences.getString(AppConfig.WEBSERVICE_ENTRYPOINT, this.resources.getString(R.string.webServicesEntryPoint))

        val jsonArrayRequest = CustomRequest(method, url, params,
                Response.Listener { response ->

                    Log.i(TAG, "Ticket response: " + response.toString())

                    // check for success status : no more 1 entry
                    if (!response.isNull(0) && response.length() < 2) {
                        // received ticket response
                        renderTicket(response)
                    } else {
                        // no ticket found
                        showNoTicket()
                    }

                },
                Response.ErrorListener { error: VolleyError ->
                    Log.e(TAG, "Error: " + error.message)
                    showNoTicket()
                }
        )

        RequestUtils.fetchRequestQueue(this)

        //add getItems to queue
        RequestUtils.addToRequestQueue(jsonArrayRequest)
    }


    private fun showNoTicket() {
        errorScanTextView.visibility = View.VISIBLE
        ticketView.visibility = View.GONE
        progressBar!!.visibility = View.GONE
    }

    /**
     * Rendering ticket details
     */
    private fun renderTicket(response: JSONArray) {
        try {

            // converting json to ticket object
            val jsonTicket= response.getJSONObject(0)

            val ticket = Gson().fromJson<Ticket>(jsonTicket.toString(), Ticket::class.java)

            if (ticket != null) {

                updateTicketView(ticket)

            } else {
                // ticket not found
                showNoTicket()
            }
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JSON Exception: " + e.message)
            showNoTicket()
            Toast.makeText(applicationContext, "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // exception
            showNoTicket()
            Toast.makeText(applicationContext, "Error occurred. Check your LogCat for full report", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTicketView(ticket: Ticket) {
        firstname.text = ticket!!.lastName
        lastname.text = ticket!!.firstName
        type.text = ticket.type
        desk.text = ticket.desk
        company.text = ticket.company
        mail.text = ticket.mail
        identifier.text = ticket.identifier
        duration.text = daysOfWeek(ticket.days)

        if (ticket.checkin.isNullOrEmpty()){
            checkin.text = this.resources.getString(R.string.first_checkin)
            checkin!!.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
        } else {
            val recordingTicket = StringBuilder()
            recordingTicket.append(this.resources.getString(R.string.last_checkin))
            recordingTicket.append(" ")
            recordingTicket.append(checkinDay(ticket.checkin))
            checkin.text = recordingTicket.toString()
            checkin.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark))
        }

        ticketView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        private val TAG = TicketResultActivity::class.java.name
    }
}



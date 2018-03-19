package com.breizhcamp.ticket.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast


import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

import org.json.JSONObject
import org.json.JSONArray


import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.breizhcamp.ticket.R
import com.breizhcamp.ticket.additions.CustomRequest
import com.breizhcamp.ticket.model.Ticket
import com.breizhcamp.ticket.utils.RequestUtils
import com.breizhcamp.ticket.view.TicketView

class TicketResultActivity : AppCompatActivity() {

    private var txtName: TextView? = null
    private var txtDesk: TextView? = null
    private var txtType: TextView? = null
    private var txtDuration: TextView? = null
    private var txtCompany: TextView? = null
    private var txtMail: TextView? = null
    private var txtError: TextView? = null
    private var progressBar: ProgressBar? = null
    private var ticketView: TicketView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_result)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        txtName = findViewById(R.id.name)
        txtType = findViewById(R.id.type)
        txtDesk = findViewById(R.id.desk)
        txtMail = findViewById(R.id.mail)
        txtCompany = findViewById(R.id.company)
        txtDuration = findViewById(R.id.duration)
        txtError = findViewById(R.id.txt_error)
        ticketView = findViewById(R.id.layout_ticket)
        progressBar = findViewById(R.id.progressBar)


        val barcode = getIntent().getStringExtra("code")

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

        val url = this.resources.getString(R.string.webServicesEntryPoint)

        val jsonArrayRequest = CustomRequest(method, url, params,
                Response.Listener { response ->

                    Log.i(TAG, "Ticket response: " + response.toString())

                    // check for success status : no more 1 entry
                    if (!response.isNull(0) && response.length() < 2) {
                        // received movie response

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
        txtError!!.visibility = View.VISIBLE
        ticketView!!.visibility = View.GONE
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

                val ownerName = StringBuilder()
                ownerName.append(ticket.lastName).append(" ").append(ticket.firstName)
                txtName!!.text = ownerName.toString()
                txtType!!.text = ticket.type
                txtDesk!!.text = ticket.desk
                txtCompany!!.text = ticket.company
                txtMail!!.text = ticket.mail

                txtDuration!!.text =  com.breizhcamp.ticket.utils.daysOfWeek(ticket.days)

                ticketView!!.visibility = View.VISIBLE
                progressBar!!.visibility = View.GONE

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



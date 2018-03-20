package com.breizhcamp.ticket.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.breizhcamp.ticket.AppConfig
import com.breizhcamp.ticket.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("ApplySharedPref")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferences = getSharedPreferences(AppConfig.SHARE_PREFERENCIES_NAME, Context.MODE_PRIVATE)

        // making toolbar transparent
        transparentToolbar()

        setContentView(R.layout.activity_main)

        scanButton.setOnClickListener {
            if(!urlTextInputLayout.editText!!.text.isNullOrEmpty()) {
                val editor = preferences.edit()
                editor.putString (AppConfig.WEBSERVICE_ENTRYPOINT, urlEditText.editableText.toString())
                editor.commit ()
                startActivity(Intent(this@MainActivity, ScanActivity::class.java))

            }else {
                urlTextInputLayout.error = this.resources.getString(R.string.error_websites_entry)
            }
        }
    }

    private fun transparentToolbar() {
        if (atLeastAPI(19) && atMostAPI(21)) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (atLeastAPI(19)) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (atLeastAPI(21)) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun atMostAPI(api: Int): Boolean {
        return api > android.os.Build.VERSION.SDK_INT
    }

    private fun atLeastAPI(api: Int): Boolean {
        return api <= android.os.Build.VERSION.SDK_INT
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}

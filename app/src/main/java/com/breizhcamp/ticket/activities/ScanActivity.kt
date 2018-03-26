package com.breizhcamp.ticket.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseArray
import android.view.MenuItem
import android.widget.Toast
import com.breizhcamp.ticket.AppConfig
import com.breizhcamp.ticket.R

import com.google.android.gms.vision.barcode.Barcode

import info.androidhive.barcode.BarcodeReader

class ScanActivity : AppCompatActivity(), BarcodeReader.BarcodeReaderListener {

    private var barcodeReader: BarcodeReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // fetch the barcode reader instance
        barcodeReader = supportFragmentManager.findFragmentById(R.id.barcode_scanner) as BarcodeReader
    }

    override fun onScanned(barcode: Barcode) {

        // ticket details activity by passing barcode
        val intent = Intent(this@ScanActivity, TicketResultActivity::class.java)
        intent.putExtra(AppConfig.CODE, barcode.displayValue)
        startActivity(intent)
    }

    override fun onScannedMultiple(list: List<Barcode>) {

    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>) {

    }

    override fun onScanError(s: String) {
        Toast.makeText(applicationContext, "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show()
    }

    override fun onCameraPermissionDenied() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

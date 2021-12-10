package com.adyen.android.assignment.utils

import android.app.Dialog
import android.content.Context
import android.util.Log
import com.adyen.android.assignment.R

object DialogUtils {

    private var progressDialog: Dialog? = null

    fun showDialogWithOKButton(context: Context, messageToDisplay : String) {
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(context)
        alertDialog.apply {
            setMessage(messageToDisplay)
            setPositiveButton(context.getString(android.R.string.ok)) { _, _ ->
                //dismiss the dialog
            }
        }.create().show()
    }

    fun showProgressDialog(context: Context?, show: Boolean) {
        if (show) {
            if (!(progressDialog != null && progressDialog?.isShowing == true)) {
                context?.let {
                    progressDialog = Dialog(it)
                }
                progressDialog?.setCancelable(false)
                progressDialog?.setContentView(R.layout.progress_bar_layout)
                try {
                    progressDialog?.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            try {
                progressDialog?.let {
                    if (it.isShowing) {
                        it.dismiss()
                    }
                }
            } catch (e: IllegalArgumentException) {
                Log.e("Error", "Exception while showing standard progress dialog")
            } finally {
                progressDialog = null
            }
        }
    }
}
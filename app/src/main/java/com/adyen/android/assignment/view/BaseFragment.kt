package com.adyen.android.assignment.view

import androidx.fragment.app.Fragment
import com.adyen.android.assignment.R
import com.adyen.android.assignment.utils.ConnectionUtils
import com.adyen.android.assignment.utils.DialogUtils.showDialogWithOKButton

open class BaseFragment : Fragment() {

    protected fun canMakeApiCall() : Boolean {
        if (!ConnectionUtils.hasInternetConnection(context)) {
            showDialogWithOKButton(requireContext(), getString(R.string.network_error_message))
            return false
        }
        return true
    }
}
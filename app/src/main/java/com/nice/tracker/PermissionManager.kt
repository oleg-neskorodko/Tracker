package com.mobilemedia.tanuki.core.utils

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionManager {

    fun checkPermission(fragment: Fragment, permissions: Array<String>): Boolean {
        var value = true
        for (permission in permissions)
            value = value && ActivityCompat.checkSelfPermission(
                fragment.requireContext(), permission
            ) == PackageManager.PERMISSION_GRANTED
        return value
    }

    fun requestPermissions(fragment: Fragment, permissions: Array<String>) {
        fragment.requestPermissions(
            permissions,
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
        )
    }

    fun shouldRationalePermissionDialog(fragment: Fragment) {
        val showRationale =
            fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (!showRationale) showNoPermissionDialog(fragment)
    }

    private fun showNoPermissionDialog(fragment: Fragment) {
        val builder = AlertDialog.Builder(fragment.requireContext())
        builder.setMessage("Need perm")
        builder.setPositiveButton("Setting") { _, _ -> openApplicationSettings(fragment) }
        builder.setNegativeButton("Cencel") { _, _ -> }
        val dialog = builder.create()
        dialog.show()
    }

    private fun openApplicationSettings(fragment: Fragment) {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + fragment.requireActivity().packageName)
        )
        fragment.startActivityForResult(appSettingsIntent, REQUEST_PERMISSION_SETTING)
    }

    companion object {
        const val REQUEST_PERMISSION_SETTING = 100
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 200
    }
}
package com.example.contacts.support

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import java.lang.ref.WeakReference

class PermissionManager(private val componentActivity: ComponentActivity) {

    private var activityWeakReference: WeakReference<Activity>? = null
    private var runnable: Runnable? = null

    private val permissionNotGrantedList = mutableListOf<String>()

    //dialog
    private var title = ""
    private var message = ""
    private var positiveButton = ""

    private var activityPermissionResult: ActivityResultLauncher<Intent>
    private var activityPermission: ActivityResultLauncher<Array<String>>

    init {
        activityPermissionResult = registerActivityResult(componentActivity)
        activityPermission = registerPermission(componentActivity)
    }

    private fun registerPermission(componentActivity: ComponentActivity): ActivityResultLauncher<Array<String>> {

        return componentActivity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            activityWeakReference?.get()?.let { activity ->

                val deniedPermissionList = mutableListOf<String>()

                it.entries.forEach { permissionResultMap ->

                    val permission = permissionResultMap.key
                    val grantReslut = permissionResultMap.value

                    if (!grantReslut) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                permission
                            )
                        ) {

                            //if the user deny permission, should request again
                            deniedPermissionList.add(permission)
                        } else {

                            //if the user deny permission twince, then GoTo app settings page
                            AlertDialog.Builder(activity).apply {

                                this.setTitle(title)
                                this.setMessage(message)
                                this.setPositiveButton(positiveButton) { _: DialogInterface?, _: Int ->

                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", activity.packageName, null)
                                    intent.data = uri
                                    activityPermissionResult.launch(intent)
                                }
                                this.create()
                                this.show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun registerActivityResult(componentActivity: ComponentActivity): ActivityResultLauncher<Intent> {

        return componentActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            activityWeakReference?.get()?.let {

                val deniedPermissionList = mutableListOf<String>()

                if (result.resultCode == Activity.RESULT_CANCELED) {

                    for (i in permissionNotGrantedList.indices) {

                        val grantResult =
                            ActivityCompat.checkSelfPermission(it, permissionNotGrantedList[i])

                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permissionNotGrantedList[i])
                        }
                    }
                }

                if (deniedPermissionList.isEmpty()) {

                    runnable?.run()
                    permissionNotGrantedList.clear()
                }

                activityWeakReference?.clear()
            }
        }
    }

    fun requestPermission(
        permissionDialogTitle: String,
        permissionDialogMessage: String,
        permissionDialogPositiveButton: String,
        permissions: Array<String>,
        runnableAfterPermissionGranted: Runnable? = null
    ) {

        permissionNotGrantedList.clear()

        for (i in permissions.indices) {

            if (ActivityCompat.checkSelfPermission(
                    componentActivity,
                    permissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                permissionNotGrantedList.add(permissions[i])
            }
        }

        if (permissionNotGrantedList.isEmpty()) {

            title = permissionDialogTitle
            message = permissionDialogMessage
            positiveButton = permissionDialogPositiveButton

            runnable = runnableAfterPermissionGranted
            activityWeakReference = WeakReference(componentActivity)
            activityPermission.launch(permissions)
        } else {

            //if all permission is granted
            runnableAfterPermissionGranted?.run()
        }
    }
}
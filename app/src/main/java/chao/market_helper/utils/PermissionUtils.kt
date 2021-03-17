package chao.market_helper.utils

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity

object PermissionUtils {
    fun getList(activity: AppCompatActivity): ArrayList<String> {
        val requestedPermissions = arrayListOf<String>()

        if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            requestedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestedPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestedPermissions.add(Manifest.permission.CAMERA)
        }

        return requestedPermissions
    }
}
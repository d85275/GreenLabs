package chao.greenlabs.views

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import chao.greenlabs.R
import chao.greenlabs.utils.PermissionUtils
import chao.greenlabs.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_main.*


private const val REQUEST_CODE_PERMISSION_RESULT = 0xFF

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_CODE_PERMISSION_RESULT) return
        for (i in permissions.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                runOnUiThread {
                    ToastUtils.show(this, getString(R.string.permission_denied))
                    finish()
                }
            }
        }
    }

    private fun checkPermissions() {
        val requestedPermissions = PermissionUtils.getList(this)
        if (requestedPermissions.isEmpty()) return

        ActivityCompat.requestPermissions(
            this,
            requestedPermissions.toTypedArray(),
            REQUEST_CODE_PERMISSION_RESULT
        )
    }

    fun showLoading() {
        ll_loading.visibility = View.VISIBLE
    }

    fun dismissLoading() {
        ll_loading.visibility = View.GONE
    }
}
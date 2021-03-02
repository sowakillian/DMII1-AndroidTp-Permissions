package com.gmail.killian.tp_permissions

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gmail.killian.tp_permissions.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        checkPermissionStatus()
        listenPermissionButtonClicked()
    }

    private fun listenPermissionButtonClicked() {
        binding.permissionButton.setOnClickListener {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.allow_popup_title))
                builder.setMessage(getString(R.string.allow_popup_message))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                    )
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                }

                builder.show()
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }
    }

    private fun checkPermissionStatus() {
        when (ContextCompat.checkSelfPermission(baseContext, Manifest.permission.ACCESS_FINE_LOCATION)) {
            PackageManager.PERMISSION_GRANTED -> {
                updatePermissionText(PermissionStatus.GRANTED)
                binding.permissionButton.isEnabled = false
                binding.permissionButton.text = getString(R.string.allow_enabled)
            }

            PackageManager.PERMISSION_DENIED -> {
                binding.permissionButton.isEnabled = true

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    updatePermissionText(PermissionStatus.DENIED)
                } else {
                    updatePermissionText(PermissionStatus.WELCOME)
                }
            }

            else -> {
                binding.permissionButton.isEnabled = true
                updatePermissionText(PermissionStatus.WELCOME)
            }
        }
    }

    private fun updatePermissionText(permissionStatus: PermissionStatus) {
        binding.permissionTextView.text = permissionStatus.text
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        checkPermissionStatus()
    }
}

enum class PermissionStatus(val text: String) {
    GRANTED("La permission de localisation est acceptée"),
    DENIED("Vous devez autoriser l'accès au GPS pour qu'on puisse récupérer votre position"),
    WELCOME("Bienvenue sur notre application, pour avoir votre position cliquer sur le bouton Autoriser l'accès")
}

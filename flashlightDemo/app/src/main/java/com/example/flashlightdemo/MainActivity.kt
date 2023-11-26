package com.example.flashlightdemo

import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {
    private lateinit var flashlight: ToggleButton
    private var isOn = false

    private lateinit var cameraManager: CameraManager
    private var cameraID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashlight = findViewById(R.id.tog_Button)
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            // The device doesn't have a camera flash
            Toast.makeText(this, "No flash available", Toast.LENGTH_SHORT).show()
            flashlight.isEnabled = false
            return
        }

        try {
            cameraID = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        flashlight.setOnCheckedChangeListener { _, isChecked ->
            isOn = isChecked
            lightOn(isOn)
        }
    }

    override fun onPause() {
        super.onPause()
        // Turn off the flashlight when the app is paused
        if (isOn) {
            lightOn(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Turn off the flashlight when the app is closed
        if (isOn) {
            lightOn(false)
        }
    }

    private fun lightOn(state: Boolean) {
        try {
            cameraID?.let { cameraManager.setTorchMode(it, state) }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            Toast.makeText(this, "Error accessing camera", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error turning on flashlight", Toast.LENGTH_SHORT).show()
        }
    }
}

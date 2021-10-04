package com.tinkoff.hw1.util

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.registerForPermission(
    callback: ActivityResultCallback<Boolean>
) = registerForActivityResult(ActivityResultContracts.RequestPermission(), callback)

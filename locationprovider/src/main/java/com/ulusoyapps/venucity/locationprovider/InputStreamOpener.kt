package com.ulusoyapps.venucity.locationprovider

import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import com.ulusoyapps.venucity.locationprovider.entity.*
import java.io.*
import javax.inject.Inject

class InputStreamOpener
@Inject constructor(
    private val context: Context
) {
    fun openStream(@RawRes csvFileResId: Int): InputStream? {
        val uri = Uri.parse("android.resource://${context.packageName}/$csvFileResId")
        return context.contentResolver.openInputStream(uri)
    }
}

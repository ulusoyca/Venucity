/*
 *  Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ulusoyapps.venucity.locationprovider

import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import java.io.InputStream
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

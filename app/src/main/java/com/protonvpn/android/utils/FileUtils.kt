/*
 * Copyright (c) 2020 Proton Technologies AG
 *
 * This file is part of ProtonVPN.
 *
 * ProtonVPN is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonVPN is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonVPN.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.protonvpn.android.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.protonvpn.android.ProtonApplication
import java.io.FileNotFoundException

object FileUtils {

    inline fun <reified T> getObjectFromAssetsWithGson(jsonAssetPath: String): T =
        getObjectFromAssets(jsonAssetPath) { json ->
            val listType = object : TypeToken<T>() {}.type
            GsonBuilder().create().fromJson(json, listType)
        }

    inline fun <reified T> getObjectFromAssetsWithJackson(jsonAssetPath: String): T =
        getObjectFromAssets(jsonAssetPath) { json ->
            Json.MAPPER.readValue(json, object : TypeReference<T>() {})
        }

    fun <T> getObjectFromAssets(jsonAssetPath: String, jsonToObject: (String) -> T): T {
        val manager = ProtonApplication.getAppContext().assets
        try {
            val file = manager.open(jsonAssetPath)
            val size = file.available()
            val buffer = ByteArray(size)
            file.read(buffer)
            val json = buffer.toString(Charsets.UTF_8)
            return jsonToObject(json)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            throw e
        }
    }
}

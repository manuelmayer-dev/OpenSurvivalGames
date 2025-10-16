package dev.suchbyte.openSurvivalGames.core.config

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class ConfigLoader {
    companion object {
        val gson: Gson = GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        fun <T> saveConfig(dataFolder: File, pluginConfig: T) {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs()
            }

            val configFile = File(dataFolder, "config.json")

            try {
                FileWriter(configFile).use { writer ->
                    gson.toJson(pluginConfig, writer)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw IOException("Failed to save configuration")
            }
        }

        inline fun <reified T> loadConfig(dataFolder: File, defaultConfig: T): T {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs()
            }

            val configFile = File(dataFolder, "config.json")

            if (!configFile.exists()) {
                saveConfig(dataFolder, defaultConfig)
            }

            try {
                FileReader(configFile).use { reader ->
                    return gson.fromJson(reader, object : TypeToken<T>() {}.type)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                throw IOException()
            }
        }
    }
}
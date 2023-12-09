package com.github.motoshige021.sunflowercopyapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.github.motoshige021.sunflowercopyapp.data.AppDatabase
import com.github.motoshige021.sunflowercopyapp.data.Plant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.github.motoshige021.sunflowercopyapp.utilties.TAG

class SeedDatabaseWorker (
    context: Context,
    workerParams: WorkerParameters
    ) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILE_NAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val plantType = object: TypeToken<List<Plant>>() {}.type
                        val plantList: List<Plant> = Gson().fromJson(jsonReader, plantType)
                        val dataBase = AppDatabase.getInstance(applicationContext)
                        dataBase.plantDao().insertAll(plantList)
                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch(ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        const val KEY_FILE_NAME = "PLANT_DATA_FILENAME"
    }
}
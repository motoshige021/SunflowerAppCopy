package com.github.motoshige021.sunflowercopyapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.github.motoshige021.sunflowercopyapp.utilties.DATABASE_NAME
import com.github.motoshige021.sunflowercopyapp.utilties.PLANT_DATA_FILE_NAME
import com.github.motoshige021.sunflowercopyapp.worker.SeedDatabaseWorker

@Database(entities = [GardenPlanting::class, Plant::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenPlantingDao() : GardenPlantingDao
    abstract fun plantDao() : PlantDao

    companion object {
        // @Volatile : スレッドからアクセスされるたび、必ず、共有メモリ上の変数の値とスレッド上の値を一致させる
        // 複数スレッドからアクセスされる可能性がある場合、@Volatileとして宣言しておくと良い
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(context).also { instance = it }
            }
        }

        private fun buildDataBase(context: Context) : AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java,
                                        DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override public fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val requet = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                .setInputData(workDataOf(SeedDatabaseWorker.KEY_FILE_NAME to PLANT_DATA_FILE_NAME))
                                .build()
                            WorkManager.getInstance(context).enqueue(requet)
                        }
                    }
                )
                .build()
        }
    }
}
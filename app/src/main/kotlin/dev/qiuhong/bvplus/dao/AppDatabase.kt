package dev.qiuhong.bvplus.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import dev.qiuhong.bvplus.BuildConfig
import dev.qiuhong.bvplus.entity.db.SearchHistoryDB
import mu.KotlinLogging
import java.util.Date
import java.util.concurrent.Executors

@Database(
    entities = [SearchHistoryDB::class],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        private var instance: AppDatabase? = null
        private val logger = KotlinLogging.logger { }

        @Suppress("unused")
        fun reset() {
            instance = null
        }

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let { return it }
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "AppDatabase.db"
            )
                .setQueryCallback(object : QueryCallback {
                    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                        if (BuildConfig.DEBUG) logger.info { "SQL Query: $sqlQuery SQL Args: $bindArgs" }
                    }
                }, Executors.newSingleThreadExecutor())
                .build()
                .apply { instance = this }
        }
    }
}

private object Converters {
    @TypeConverter
    fun timestampToDate(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}
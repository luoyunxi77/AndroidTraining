package com.example.androidtraining.model

import android.content.Context
import androidx.room.*

@Entity
data class DownloadModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val urlIndex: Int,
    val progress: Int,
    val createTime: Long
)

@Dao
interface DownloaderDao {
    @Query("SELECT * FROM DownloadModel ORDER BY createTime DESC LIMIT 1")
    fun getLastDownloadModel(): DownloadModel

    @Query("SELECT * FROM DownloadModel")
    fun getLastDownloadModels(): List<DownloadModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewDownload(vararg models: DownloadModel)
}

@Database(entities = [DownloadModel::class], version = 1)
abstract class DownLoadDatabase : RoomDatabase() {
    abstract fun downloadDao(): DownloaderDao

    companion object {
        private var DATABASE: DownLoadDatabase? = null
        fun getDownloadDatabaseInstance(context: Context): DownLoadDatabase =
            DATABASE?.let { it } ?: Room.databaseBuilder(
                context,
                DownLoadDatabase::class.java, "download_db"
            ).allowMainThreadQueries().build().apply { DATABASE = this }
    }
}


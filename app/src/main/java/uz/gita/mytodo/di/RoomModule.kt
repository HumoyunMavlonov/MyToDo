package uz.gita.mytodo.di

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.mytodo.data.local.TaskDatabase
import uz.gita.mytodo.data.local.dao.TaskDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room
            .databaseBuilder(
                context = context,
                klass = TaskDatabase::class.java,
                name = TaskDatabase.DATABASE_NAME
            )
            .build()

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.getTaskDao()

    @Provides
    @Singleton
    fun provNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

}
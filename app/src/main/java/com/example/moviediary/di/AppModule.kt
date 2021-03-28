package com.example.moviediary.di

import android.app.Application
import androidx.room.Room
import com.example.moviediary.data.MovieDiaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application, callback: MovieDiaryDatabase.Callback)
        = Room.databaseBuilder(app, MovieDiaryDatabase::class.java, "movie_diary_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideNoteDao(db: MovieDiaryDatabase) = db.filmDao()

    @Provides
    fun provideTagDao(db: MovieDiaryDatabase) = db.producerDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
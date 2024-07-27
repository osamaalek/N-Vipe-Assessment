package com.osamaalek.n_vipeassessment.di

import android.app.Application
import android.content.Context
import com.osamaalek.n_vipeassessment.repository.DirectionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun providesDirectionsRepository(): DirectionsRepository {
        return DirectionsRepository()
    }
}

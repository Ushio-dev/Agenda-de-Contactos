package com.example.practicando.contactos.data.di

import android.content.Context
import androidx.room.Room
import com.example.practicando.contactos.data.ContactoDao
import com.example.practicando.contactos.data.ContactosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun providesDao(contactosDatabase: ContactosDatabase): ContactoDao {
        return contactosDatabase.contactoDao()
    }

    @Provides
    @Singleton
    fun provideContactoDatabase(@ApplicationContext applicationContext: Context): ContactosDatabase {
        return Room.databaseBuilder(applicationContext, ContactosDatabase::class.java, "ContactoDatabase").build()
    }
}
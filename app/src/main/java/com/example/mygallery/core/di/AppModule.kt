package com.example.mygallery.core.di

import com.example.mygallery.data.repository.GalleryRepositoryImpl
import com.example.mygallery.domain.repository.GalleryRepository
import org.koin.dsl.module

val appModule = module {
    single<GalleryRepository> { GalleryRepositoryImpl(get()) }
}
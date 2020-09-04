package com.service.codingtest.module

import com.service.codingtest.view.adapters.ImageAdapter
import com.service.codingtest.view.adapters.ImageLoadStateAdapter
import org.koin.dsl.module

val appModule = module {
    factory { ImageLoadStateAdapter(get()) }
    single { ImageAdapter() }
}
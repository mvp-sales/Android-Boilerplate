package com.mvpsales.github.di

import com.mvpsales.github.ui.newsdetail.NewsDetailViewModel
import com.mvpsales.github.ui.newslist.NewsListViewModel
import com.mvpsales.github.ui.newssaved.NewsSavedViewModel
import com.mvpsales.github.ui.sourceslist.SourcesListViewModel
import com.mvpsales.github.utils.DispatcherHelper
import com.mvpsales.github.utils.DispatcherHelperImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    single<DispatcherHelper> { DispatcherHelperImpl() }
    viewModel { (searchTerm: String) -> NewsListViewModel(searchTerm, get(), get()) }
    viewModel { NewsDetailViewModel(get(), get()) }
    viewModel { NewsSavedViewModel(get(), get()) }
    viewModel { SourcesListViewModel(get(), get()) }
}
package com.gstormdev.stepbuddy.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gstormdev.stepbuddy.ui.main.MainViewModel
import com.gstormdev.stepbuddy.viewmodel.FitViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: FitViewModelFactory): ViewModelProvider.Factory
}
package com.example.busapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.busapp.models.AppData
import com.example.busapp.models.BusStop
import com.example.busapp.models.UserData
import com.example.busapp.viewmodels.AddBusStopViewModel
import com.example.busapp.viewmodels.GtfsRealTimeViewModel
import com.example.busapp.viewmodels.UserViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

@FlowPreview
val userDataAccessModule = module {
    single<Storage<UserData>> {
        PersistentStorage(
            gson = get(),
            type = object: TypeToken<List<UserData>>(){}.type,
            preferenceKey = stringPreferencesKey("user"),
            dataStore = androidContext().dataStore
        )
    }


    single { Gson() }

    viewModel {
        UserViewModel(
            userDataStorage = get()
        )
    }

    viewModel {
        AddBusStopViewModel()
    }

    viewModel {
        GtfsRealTimeViewModel()
    }

}
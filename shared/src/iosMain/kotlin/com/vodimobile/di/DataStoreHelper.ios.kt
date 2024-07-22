package com.vodimobile.di

import com.vodimobile.data.data_store.UserDataStoreRepositoryImpl
import com.vodimobile.domain.storage.data_store.UserDataStoreStorage
import com.vodimobile.domain.use_case.data_store.EditUserDataStoreUseCase
import com.vodimobile.domain.use_case.data_store.GetUserDataUseCase
import com.vodimobile.utils.data_store.getDataStore

class DataStoreHelper {

    private val userDataStoreRepository = UserDataStoreRepositoryImpl(dataStore = getDataStore())

    val userDataStoreStorage = UserDataStoreStorage(
        editUserDataStoreUseCase = EditUserDataStoreUseCase(userDataStoreRepository),
        getUserDataUseCase = GetUserDataUseCase(userDataStoreRepository)
    )
}
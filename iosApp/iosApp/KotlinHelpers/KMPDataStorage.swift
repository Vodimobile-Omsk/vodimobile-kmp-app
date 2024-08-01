//
//  UserDataStorage.swift
//  iosApp
//
//  Created by Sergey Ivanov on 23.07.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import shared
import SwiftUI

final class KMPDataStorage: ObservableObject {
    private let repository = UserDataStoreRepositoryImpl(dataStore: CreateDataStore_iosKt.createDataStore())
    let newUser = User(
        fullName: "test testov",
        password: "12344321",
        token: "token_test",
        phone: "+79029994148",
        email: "rele@df.df"
    )
    static let defaultUser = User.companion.empty()

    @Published var gettingUser: User = KMPDataStorage.defaultUser

    func editUserData(_ userData: User) async throws {
        try await repository.editUserData(user: userData)
    }
    
    @MainActor
    func getUser() async throws {
        let usersFlow = repository.getUserData()
        let users = usersFlow.map { $0 }
        
        for await flowUser in repository.getUserData() {
            self.gettingUser = flowUser
        }
    }
}

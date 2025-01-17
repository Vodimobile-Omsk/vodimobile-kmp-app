//
//  AuthScreenView.swift
//  iosApp
//
//  Created by Sergey Ivanov on 09.07.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct AuthScreenView: View {
    @State private var checkboxSelected: Bool = false
    @State private var isButtonEnabled: Bool = false
    @Binding var showSignSuggestModal: Bool
    @StateObject private var viewModel = UserDataViewModel.shared

    @Environment(\.dismiss) private var dismiss

    var body: some View {
        VStack(spacing: AuthAndRegScreensConfig.spacingBetweenGroupAndCheckbox) {
            VStack(spacing: AuthAndRegScreensConfig.spacingBetweenComponents) {
                BorderedTextField(
                    fieldContent: $viewModel.phoneField,
                    isValid: $viewModel.isPhoneValid,
                    fieldType: .phone,
                    inputErrorType: $viewModel.inputError
                )
                .onChange(of: viewModel.isPhoneValid) { _ in
                    toggleButtonEnabled()
                }

                BorderedTextField(
                    fieldContent: $viewModel.passwordField,
                    isValid: $viewModel.isPasswordValid,
                    fieldType: .password,
                    inputErrorType: $viewModel.inputError,
                    isForgetBtnEnabled: true
                )
                .onChange(of: viewModel.isPasswordValid) { _ in
                    toggleButtonEnabled()
                }

                NavigationLink(destination: PinCodeView(
                    showSignSuggestModal: $showSignSuggestModal,
                    authFlowType: .auth,
                    phoneNumber: viewModel.phoneField,
                    pass: viewModel.passwordField) {
                        viewModel.cleanAllFields()
                    }
                ) {
                    Text(R.string.localizable.nextBtnName)
                }
                .buttonStyle(FilledBtnStyle())
                .disabled(!isButtonEnabled)
            }

            HStack(spacing: spacingBetweenCheckboxAndText) {
                CheckboxView(isChecked: $checkboxSelected)
                    .onChange(of: checkboxSelected) { _ in
                        toggleButtonEnabled()
                    }

                VStack(alignment: .leading) {
                    Text(R.string.localizable.conditionText)
                        .font(.paragraph5)
                        .foregroundStyle(Color(R.color.grayDark))

                    NavigationLink(destination: ConditionScreenView()) {
                        Text(R.string.localizable.conditionLink)
                            .foregroundColor(Color(R.color.blueColor))
                            .font(.buttonCheckBox)
                    }
                }
                Spacer()
            }
            Spacer()
        }
        .padding(.horizontal, horizontalPadding)
        .padding(.top, аuthScreencontentTopPadding)
        .navigationBarBackButtonHidden()
        .toolbar {
            CustomToolbar(title: R.string.localizable.authScreenTitle)
        }
    }

    private func toggleButtonEnabled() {
        isButtonEnabled = viewModel.isPhoneValid && checkboxSelected
    }
}

#Preview {
    AuthScreenView(showSignSuggestModal: Binding.constant(false))
}

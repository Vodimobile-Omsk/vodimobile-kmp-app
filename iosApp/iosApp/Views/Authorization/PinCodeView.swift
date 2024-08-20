//
//  ConfirmationScreenView.swift
//  iosApp
//
//  Created by Sergey Ivanov on 10.07.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct PinCodeView: View {
    @State private var pin: [String] = ["", "", "", ""]
    @FocusState private var focusedField: Int?
    @State private var isButtonEnabled: Bool = false
    @Binding var showSignSuggestModal: Bool
    private let authFlowType: AuthFlowType
    private var sendCodeOnPhoneText: String {
        "\(R.string.localizable.sendCodeMsg())\n\(phoneNumber)"
    }
    private let fullname: String
    private let phoneNumber: String
    private let pass: String
    private let authManager = AuthManager.shared
    @Environment(\.dismiss) private var dismiss

    init(
        showSignSuggestModal: Binding<Bool>,
        authFlowType: AuthFlowType,
        phoneNumber: String,
        pass: String,
        fullname: String? = nil
    ) {
        self._showSignSuggestModal = showSignSuggestModal
        self.fullname = fullname ?? ""
        self.phoneNumber = phoneNumber
        self.pass = pass
        self.authFlowType = authFlowType
    }

    var body: some View {
        VStack(spacing: PinCodeConfig.spacingBetweenGroupAndResendText) {
            VStack(spacing: PinCodeConfig.spacingBetweenMainComponents) {
                Text(R.string.localizable.inputCodeText)
                    .font(.header2)
                    .padding(.top, PinCodeConfig.contentTopPadding)
                    .foregroundColor(Color.black)
                    .multilineTextAlignment(.center)

                Text(sendCodeOnPhoneText)
                    .font(.paragraph2)
                    .foregroundColor(Color(R.color.grayText))
                    .multilineTextAlignment(.center)

                HStack(spacing: PinCodeConfig.spacingBetweenPincodeCells) {
                    ForEach(0..<4) { index in
                        PinCodeTextField(index: index)
                    }
                }
                .padding(.vertical, PinCodeConfig.verticalSpacingBetweenPincodeField)
                .onChange(of: pin) { _ in
                    toggleButtonEnabled()
                }

                switch authFlowType {
                case .registration:
                    Button(R.string.localizable.nextBtnName(), action: {
                        Task {
                            await authManager.signUp(
                                fullname: fullname,
                                phone: phoneNumber,
                                password: pass
                            )
                        }
                        showSignSuggestModal.toggle()
                    })
                    .buttonStyle(FilledBtnStyle())
                    .disabled(!isButtonEnabled)
                case .auth:
                    Button(R.string.localizable.nextBtnName(), action: {
                        Task {
                            await authManager.login(phone: phoneNumber, pass: pass)
                        }
                        showSignSuggestModal.toggle()
                    })
                    .buttonStyle(FilledBtnStyle())
                    .disabled(!isButtonEnabled)
                case .resetPassword:
                    NavigationLink(destination: ResetPasswordPassView()) {
                        Text(R.string.localizable.nextBtnName)
                    }
                    .buttonStyle(FilledBtnStyle())
                    .disabled(!isButtonEnabled)
                }
            }

            HStack {
                Text(R.string.localizable.notGetCodeText)
                    .foregroundColor(.black)
                    .font(.paragraph4)
                Button(
                    action: {
                        print("Отправить код повторно нажат")
                    }
                ) {
                    Text(R.string.localizable.resendBtnText)
                        .foregroundColor(Color(R.color.blueColor))
                        .font(.buttonText)
                        .underline()
                }
            }

            Spacer()
        }
        .padding()
        .onAppear {
            focusedField = 0
        }
        .navigationBarBackButtonHidden()
        .toolbar {
            CustomToolbar(title: R.string.localizable.confirmScreenTitle)
        }
    }

    @ViewBuilder
    private func PinCodeTextField(index: Int) -> some View {
        let isFieldFocused = focusedField == index
        let strokeColor = isFieldFocused ? Color(R.color.blueColor) : Color(R.color.grayDark)
        let lineWidth: CGFloat = pin[index].isEmpty && !isFieldFocused ? 0 : 2

        TextField("", text: $pin[index])
            .keyboardType(.numberPad)
            .multilineTextAlignment(.center)
            .font(.paragraph1)
            .frame(width: 56, height: 56)
            .background(
                RoundedRectangle(cornerRadius: 12, style: .continuous)
                    .fill(Color(R.color.grayLight))
            )
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(strokeColor, lineWidth: lineWidth)
            )
            .focused($focusedField, equals: index)
            .onChange(of: pin[index]) { newValue in
                if newValue.count == 1, index < 3 {
                    focusedField = index + 1
                } else if newValue.isEmpty, index > 0 {
                    focusedField = index - 1
                } else if newValue.count > 1 {
                    pin[index] = String(newValue.prefix(1))
                } else if index == pin.count - 1 && !pin[index].isEmpty {
                    focusedField = nil
                }
            }
    }

    private func toggleButtonEnabled() {
        isButtonEnabled = pin.joined().count == 4
    }
}

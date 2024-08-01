//
//  AutoCardView.swift
//  iosApp
//
//  Created by Sergey Ivanov on 24.07.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI
import shared

struct AutoCardWithButtonView: View {
    let autoCard: Car
    @Binding var showModal: Bool
    @Binding var selectedAuto: Car
    private var carPreview: Image
    private var carPrice: Float
    
    init(autoCard: Car, selectedAuto: Binding<Car>, showModal: Binding<Bool>) {
        self.autoCard = autoCard
        if let image = autoCard.images.first, let tariff = autoCard.tariffs.first {
            self.carPreview = Image(ImageResource(name: image.assetImageName, bundle: image.bundle))
            self.carPrice = tariff.cost
        } else {
            self.carPreview = Image.questionFolder
            self.carPrice = 0
        }
        self._selectedAuto = selectedAuto
        self._showModal = showModal
    }
    
    var body: some View {
        VStack(spacing: 12) {
            carPreview
                .resizable()
                .aspectRatio(contentMode: .fit)
                .padding(.horizontal, 25)
            
            HStack {
                VStack(alignment: .leading, spacing: 8) {
                    Text(autoCard.model.resource).font(.header3)
                    Text("от \(Int(carPrice)) руб.")
                        .font(.header4)
                        .fontWeight(.bold)
                        .foregroundStyle(Color(R.color.blueColor))
                        .multilineTextAlignment(.leading)
                }
                Spacer()
            }
            
            HStack {
                Button(R.string.localizable.bookButton()) { }
                    .buttonStyle(FilledBtnStyle(heightButton: 40))
                    .padding(.trailing, 20)
                ZStack {
                    Image.infoCircleFill
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .foregroundStyle(Color(R.color.grayDarkColor))
                        .frame(width: 20, height: 20)
                }
                .frame(width: 40, height: 40)
                .background(RoundedRectangle(cornerRadius: 10).fill(Color(R.color.grayLightColor)))
                .onTapGesture {
                    selectedAuto = autoCard
                    showModal = true
                }
            }
        }
        .padding(.horizontal, 32)
        .padding(.vertical, 24)
        .background(RoundedRectangle(cornerRadius: 24).fill(.white))
    }
}

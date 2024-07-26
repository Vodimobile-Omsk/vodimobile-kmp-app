//
//  ExpantableToolbar.swift
//  iosApp
//
//  Created by Sergey Ivanov on 24.07.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import SwiftUI

struct ExpandableToolbar: View {
    @Environment(\.calendar) var calendar
    @Binding var isExpanded: Bool
    @Binding var dateRange: ClosedRange<Date>?
    @Binding var showDatePicker: Bool
    @Binding var headerHeight: CGFloat
    @Binding var dragOffset: CGSize
    
    var body: some View {
        VStack(spacing: 0) {
            GeometryReader { geometry in
                VStack(spacing: 0) {
                    HStack {
                        Spacer()
                        
                        Button(action: {
                            // Action for bell button
                        }) {
                            Image.bell
                                .resizable()
                                .frame(width: 20, height: 24)
                                .foregroundColor(.white)
                        }
                    }
                    .padding(.horizontal)
                    .padding(.top, 55)
                    .background(Color(R.color.blueDarkColor))
                    
                    if isExpanded {
                        VStack {
                            VStack(spacing: 20) {
                                Text(R.string.localizable.dateTextFieldTitle)
                                    .font(.header3)
                                
                                Button(action: {
                                    showDatePicker = true
                                }) {
                                    HStack(spacing: 10) {
                                        Image(R.image.calendar)
                                            .resizable()
                                            .aspectRatio(contentMode: .fit)
                                            .frame(width: 30 , height: 30)
                                            .foregroundColor(.gray)
                                        
                                        Text(formatDateRange())
                                            .foregroundColor(dateRange == nil ? .gray : .black)
                                        Spacer()
                                    }
                                    .frame(alignment: .leading)
                                    .padding(.all, 16)
                                    .background(
                                        RoundedRectangle(cornerRadius: 10)
                                            .stroke(Color.gray, lineWidth: 1)
                                    )
                                }
                                
                                Button(R.string.localizable.findAutoButton()) {
                                    // TODO: - Make a navigation link into view
                                }
                                .buttonStyle(FilledBtnStyle())
                            }
                            .padding(.vertical, 20)
                            .padding(.horizontal, 16)
                            .background(RoundedRectangle(cornerRadius: 24).fill(.white))
                        }
                        .padding(.vertical, 16)
                        .padding(.horizontal, 16)
                        .offset(y: isExpanded ? 0 : -UIScreen.main.bounds.height * 0.3)
                        .animation(.spring(), value: isExpanded)
                    }
                    
                    if !isExpanded {
                        Image.minus
                            .resizable()
                            .frame(width: 100, height: 5)
                            .foregroundColor(.white)
                            .padding(.vertical, 16)
                            .onTapGesture {
                                withAnimation {
                                    isExpanded.toggle()
                                }
                            }
                            .gesture(
                                DragGesture()
                                    .onChanged { gesture in
                                        if gesture.translation.height > 0 {
                                            dragOffset = gesture.translation
                                            
                                            if dragOffset.height > 50 {
                                                withAnimation {
                                                    isExpanded = true
                                                }
                                            }
                                            dragOffset = .zero
                                        }
                                    }
                            )
                    }
                }
                .background(Color(R.color.blueDarkColor))
                .cornerRadius(24)
                .animation(.spring(), value: isExpanded)
                .onAppear {
                    headerHeight = geometry.size.height
                }
            }
            .frame(height: isExpanded ? 200 : 100)
        }
    }
    
    private func formatDateRange() -> String {
        guard let dateRange = dateRange else {
            return R.string.localizable.dateTextFieldPlaceholder()
        }
        
        let formatter = DateFormatter()
        formatter.dateFormat = "dd MMMM yyyy"
        
        let startDate = formatter.string(from: dateRange.lowerBound)
        let endDate = formatter.string(from: dateRange.upperBound)
        
        if startDate == endDate {
            return startDate
        } else if calendar.compare(dateRange.lowerBound, to: dateRange.upperBound, toGranularity: .day) == .orderedAscending {
            return "\(startDate) - \(endDate)"
        } else {
            return "\(endDate) - \(startDate)"
        }
    }
}
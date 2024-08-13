package com.vodimobile.presentation.screens.orders.store

import com.vodimobile.android.R

data class OrderState(
    val tags: List<Int> = listOf(
        R.string.active_order,
        R.string.completed_order
    )
)
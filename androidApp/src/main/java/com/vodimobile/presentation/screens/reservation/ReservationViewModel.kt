package com.vodimobile.presentation.screens.reservation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vodimobile.domain.model.Car
import com.vodimobile.domain.model.order.CarStatus
import com.vodimobile.domain.model.remote.dto.bid_cost.BidCostParams
import com.vodimobile.domain.model.remote.dto.create_bid.BidCreateParams
import com.vodimobile.domain.model.remote.either.CrmEither
import com.vodimobile.domain.model.supabase.OrderDTO
import com.vodimobile.domain.storage.crm.CrmStorage
import com.vodimobile.domain.storage.data_store.UserDataStoreStorage
import com.vodimobile.domain.storage.supabase.SupabaseStorage
import com.vodimobile.presentation.screens.reservation.store.ReservationEffect
import com.vodimobile.presentation.screens.reservation.store.ReservationIntent
import com.vodimobile.presentation.screens.reservation.store.ReservationState
import com.vodimobile.presentation.utils.date_formats.increaseFreeYear
import com.vodimobile.presentation.utils.date_formats.reduceFreeYear
import com.vodimobile.utils.bid.bidGripReverse
import com.vodimobile.utils.date_formats.parseToCrmDate
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds

class ReservationViewModel(
    private val dataStoreStorage: UserDataStoreStorage,
    private val crmStorage: CrmStorage,
    private val supabaseStorage: SupabaseStorage
) : ViewModel() {

    val reservationState = MutableStateFlow(ReservationState())
    val reservationEffect = MutableSharedFlow<ReservationEffect>()
    private val supervisorCoroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + SupervisorJob()

    fun onIntent(intent: ReservationIntent) {
        when (intent) {

            ReservationIntent.GetAllCars -> {
                viewModelScope.launch(supervisorCoroutineContext) {
                    val crmEither = crmStorage.getCarList(
                        accessToken = reservationState.value.user.accessToken,
                        refreshToken = reservationState.value.user.refreshToken
                    )
                    when (crmEither) {
                        is CrmEither.CrmData -> {
                            val car =
                                crmEither.data.find { it.carId == reservationState.value.carId }
                                    ?: Car.empty()

                            reservationState.update {
                                it.copy(selectedCar = car)
                            }
                        }

                        is CrmEither.CrmError -> {}

                        CrmEither.CrmLoading -> {}
                    }
                }
            }

            ReservationIntent.GetAllPLaces -> {
                viewModelScope.launch(supervisorCoroutineContext) {
                    val crmEither = crmStorage.getPlaces(
                        accessToken = reservationState.value.user.accessToken,
                        refreshToken = reservationState.value.user.refreshToken
                    )
                    when (crmEither) {
                        is CrmEither.CrmData -> {
                            reservationState.update {
                                it.copy(placeList = crmEither.data.filter { item -> !item.archive })
                            }
                        }

                        is CrmEither.CrmError -> {}

                        CrmEither.CrmLoading -> {}
                    }
                }
            }

            ReservationIntent.GetAllServices -> {
                viewModelScope.launch(supervisorCoroutineContext) {
                    val crmEither = crmStorage.getServices(
                        accessToken = reservationState.value.user.accessToken,
                        refreshToken = reservationState.value.user.refreshToken
                    )
                    when (crmEither) {
                        is CrmEither.CrmData -> {
                            reservationState.update {
                                it.copy(serviceList = crmEither.data)
                            }
                        }

                        is CrmEither.CrmError -> {}

                        CrmEither.CrmLoading -> {}
                    }
                }
            }

            ReservationIntent.ReturnBack -> {
                viewModelScope.launch {
                    reservationEffect.emit(ReservationEffect.ReturnBack)
                }
            }

            is ReservationIntent.GetPlaceChange -> {
                reservationState.update {
                    it.copy(
                        getPlaceId = intent.value.first,
                        getPlace = intent.value.second,
                        errorGetPlace = intent.value.second.isEmpty()
                    )
                }
            }

            is ReservationIntent.ReturnPlaceChange -> {
                reservationState.update {
                    it.copy(
                        returnPlaceId = intent.value.first,
                        returnPlace = intent.value.second,
                        errorReturnPlace = intent.value.second.isEmpty()
                    )
                }
            }

            is ReservationIntent.StartTimeChange -> {
                reservationState.update {
                    it.copy(
                        startTime = intent.value, errorStartTime = intent.value.isEmpty()
                    )
                }
            }

            is ReservationIntent.EndTimeChange -> {
                reservationState.update {
                    it.copy(
                        endTime = intent.value, errorEndTime = intent.value.isEmpty()
                    )
                }
            }

            is ReservationIntent.DateChange -> {
                reservationState.update {
                    it.copy(
                        date = intent.value,
                        errorDate = intent.value.contentEquals(longArrayOf(0L, 0L))
                    )
                }
            }

            is ReservationIntent.CarIdChange -> {
                viewModelScope.launch {
                    reservationState.update {
                        it.copy(
                            carId = intent.value
                        )
                    }
                }
            }

            is ReservationIntent.ServiceIdChange -> {
                reservationState.update {
                    Log.d("TAG", it.selectedServiceIdList.joinToString(prefix = "[", postfix = "]"))
                    it.copy(
                        selectedServiceIdList =
                        if (intent.value in it.selectedServiceIdList) it.selectedServiceIdList - intent.value
                        else it.selectedServiceIdList + intent.value
                    )
                }

                getBidCostIfPossible()
            }

            is ReservationIntent.GetCarFreeDate -> {
                viewModelScope.launch(supervisorCoroutineContext) {
                    val start =
                        if (intent.value[0] != 0L) intent.value[0] else reduceFreeYear()
                    val end =
                        if (intent.value[1] != 0L) intent.value[1] else increaseFreeYear()

                    val freeDates = crmStorage.getCarFreeDateRange(
                        accessToken = reservationState.value.user.accessToken,
                        refreshToken = reservationState.value.user.refreshToken,
                        carId = reservationState.value.carId,
                        begin = start,
                        end = end
                    )
                    reservationState.update {
                        it.copy(freeDates = freeDates)
                    }
                    reservationEffect.emit(ReservationEffect.EmitGeneralStateChange)
                }
            }

            is ReservationIntent.ShowTimePicker -> {
                viewModelScope.launch {
                    reservationEffect.emit(ReservationEffect.ShowTimePicker(intent.type))
                }
            }

            ReservationIntent.ShowLoadingDialog -> {
                viewModelScope.launch {
                    reservationEffect.emit(ReservationEffect.ShowLoadingDialog)
                }
            }

            ReservationIntent.DismissLoadingDialog -> {
                viewModelScope.launch {
                    reservationEffect.emit(ReservationEffect.DismissLoadingDialog)
                }
            }

            ReservationIntent.ShowDatePicker -> {
                viewModelScope.launch {
                    reservationEffect.emit(ReservationEffect.ShowDatePicker)
                }
            }

            ReservationIntent.GetBidCost -> {
                getBidCostIfPossible()
            }

            ReservationIntent.PutBid -> {
                viewModelScope.launch(supervisorCoroutineContext) {
                    reservationEffect.emit(ReservationEffect.ShowLoadingDialog)
                    val crmEither = crmStorage.createBid(
                        accessToken = reservationState.value.user.accessToken,
                        refreshToken = reservationState.value.user.refreshToken,
                        bidCreateParams = BidCreateParams(
                            fio = reservationState.value.user.fullName,
                            phone = reservationState.value.user.phone,
                            car_id = reservationState.value.carId,
                            begin = "${toFormat2(reservationState.value.date[0])} ${reservationState.value.startTime}",
                            end = "${toFormat2(reservationState.value.date[1])} ${reservationState.value.endTime}",
                            begin_place_id = reservationState.value.getPlaceId,
                            end_place_id = reservationState.value.returnPlaceId,
                            services = reservationState.value.selectedServiceIdList.map { it.service_id }.ifEmpty { null },
                            prepayment = null,
                            files = null
                        )
                    )
                    delay(1.seconds)
                    when (crmEither) {
                        is CrmEither.CrmData -> {
                            supabaseStorage.insertOrder(
                                orderDTO = OrderDTO(
                                    bid_id = crmEither.data.bid_id ?: 0,
                                    bid_number = crmEither.data.bid_number ?: 0,
                                    user_id = reservationState.value.user.id,
                                    car_id = reservationState.value.carId,
                                    crm_bid_id = crmEither.data.bid_id ?: 0,
                                    bid_status = bidGripReverse[CarStatus.Processing]
                                        ?: "Отменено",
                                    date_start = toFormat(reservationState.value.date[0]),
                                    date_finish = toFormat(reservationState.value.date[1]),
                                    time_start = reservationState.value.startTime,
                                    time_finish = reservationState.value.endTime,
                                    place_start = reservationState.value.getPlace.split(" - ")[0],
                                    place_finish = reservationState.value.returnPlace.split(" - ")[0],
                                    cost = reservationState.value.bidCost.toFloat(),
                                    services = if (reservationState.value.selectedServiceIdList.isNotEmpty()) reservationState.value.selectedServiceIdList.joinToString(
                                        ", "
                                    ) else ""
                                )
                            )

                            reservationEffect.emit(ReservationEffect.DismissLoadingDialog)
                            reservationEffect.emit(ReservationEffect.PutBid)
                            reservationEffect.emit(ReservationEffect.Success)
                        }

                        is CrmEither.CrmError -> {
                            reservationEffect.emit(ReservationEffect.DismissLoadingDialog)
                            reservationEffect.emit(ReservationEffect.Fail)
                        }

                        CrmEither.CrmLoading -> {
                            reservationEffect.emit(ReservationEffect.DismissLoadingDialog)
                        }
                    }
                }
            }

            ReservationIntent.InitUser -> {
                viewModelScope.launch(supervisorCoroutineContext) {
                    dataStoreStorage.getUser().collect { value ->
                        val user =
                            supabaseStorage.getUser(password = value.password, phone = value.phone)
                        reservationState.update {
                            it.copy(user = user)
                        }
                    }
                }
            }
        }
    }

    private fun getBidCostIfPossible() {
        if (
            reservationState.value.getPlaceId > 0 &&
            reservationState.value.returnPlaceId > 0 &&
            reservationState.value.startTime.isNotEmpty() &&
            reservationState.value.endTime.isNotEmpty() &&
            reservationState.value.date[0] != 0L &&
            reservationState.value.date[1] != 0L
        ) {
            viewModelScope.launch(supervisorCoroutineContext) {
                val crmEither = crmStorage.getBidCost(
                    accessToken = reservationState.value.user.accessToken,
                    refreshToken = reservationState.value.user.refreshToken,
                    bidCostParams = BidCostParams(
                        car_id = reservationState.value.carId,
                        begin = "${reservationState.value.date[0].parseToCrmDate()} ${reservationState.value.startTime}",
                        end = "${reservationState.value.date[1].parseToCrmDate()} ${reservationState.value.endTime}",
                        begin_place_id = reservationState.value.getPlaceId,
                        end_place_id = reservationState.value.returnPlaceId,
                        services = reservationState.value.selectedServiceIdList.map { it.service_id }.toTypedArray()
                    )
                )
                when (crmEither) {
                    is CrmEither.CrmData -> {
                        reservationState.update {
                            it.copy(bidCost = crmEither.data.cost.toString())
                        }
                    }

                    is CrmEither.CrmError -> {}

                    CrmEither.CrmLoading -> {}
                }
            }
        }
    }

    private fun toFormat(date: Long): String {
        return SimpleDateFormat(
            "dd.MM.yyyy", Locale.getDefault()
        ).format(date)
    }

    private fun toFormat2(date: Long): String {
        return SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault()
        ).format(date)
    }
}
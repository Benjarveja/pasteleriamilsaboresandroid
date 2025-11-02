package com.example.pasteleriamilssaboresandroid.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.database.user.User
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.domain.model.Order
import com.example.pasteleriamilssaboresandroid.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

data class CheckoutUiState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val pricing: com.example.pasteleriamilssaboresandroid.util.PricingResult,
    val validationErrors: Map<String, String> = emptyMap(),
    val isFormValid: Boolean = false
)

data class CheckoutFormData(
    val deliveryOption: String = "delivery",
    val street: String = "",
    val comuna: String = "",
    val region: String = "",
    val branch: String = "",
    val pickupDate: String = "",
    val pickupTimeSlot: String = "",
    val paymentMethod: String = "",
    val notes: String = "",
    val run: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val couponCode: String = ""
)

class CheckoutViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState(pricing = computeDiscounts(0, null)))
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _formData = MutableStateFlow(CheckoutFormData())
    val formData: StateFlow<CheckoutFormData> = _formData.asStateFlow()

    fun loadForm(user: User?, subtotal: Int) {
        val coupon = _formData.value.couponCode
        val pricing = computeDiscounts(subtotal, coupon.ifEmpty { null })
        _uiState.update { it.copy(pricing = pricing) }

        _formData.update {
            if (user != null) {
                it.copy(
                    run = user.run,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    phone = user.phone,
                    birthDate = user.birthDate,
                    street = user.street,
                    comuna = user.comuna,
                    region = user.region
                )
            } else {
                // Clear user-specific data if user is null (logged out)
                CheckoutFormData(couponCode = coupon) // Keep coupon
            }
        }
        validateForm()
    }

    fun updateFormData(newFormData: CheckoutFormData) {
        _formData.value = newFormData
        validateForm()
    }

    fun applyCoupon(subtotal: Int) {
        val pricing = computeDiscounts(subtotal, _formData.value.couponCode)
        _uiState.update { it.copy(pricing = pricing) }
    }

    private fun validateForm() {
        val errors = mutableMapOf<String, String>()
        val data = _formData.value

        if (!isValidRun(data.run)) errors["run"] = "RUN no válido"
        if (!isNonEmpty(data.firstName)) errors["firstName"] = "El nombre es requerido"
        if (!isNonEmpty(data.lastName)) errors["lastName"] = "El apellido es requerido"
        if (!isValidEmail(data.email)) errors["email"] = "Email no válido"
        if (!isValidChileanPhone(data.phone)) errors["phone"] = "Teléfono no válido"
        if (!isValidBirthDate(data.birthDate)) errors["birthDate"] = "Fecha de nacimiento no válida"

        if (data.deliveryOption == "delivery") {
            if (!isNonEmpty(data.street)) errors["street"] = "La calle es requerida"
            if (!isNonEmpty(data.comuna)) errors["comuna"] = "La comuna es requerida"
            if (!isNonEmpty(data.region)) errors["region"] = "La región es requerida"
        } else {
            if (!isNonEmpty(data.branch)) errors["branch"] = "La sucursal es requerida"
            if (!isNonEmpty(data.pickupTimeSlot)) errors["pickupTimeSlot"] = "El horario es requerido"
        }

        _uiState.update { it.copy(validationErrors = errors, isFormValid = errors.isEmpty()) }
    }

    fun processOrder(items: List<CartItem>, user: User?) {
        validateForm()
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val order = Order(
                code = "MS-${Date().time.toString().takeLast(6)}",
                createdAt = Date().toString(),
                userId = user?.id?.toString(),
                items = items,
                subtotal = uiState.value.pricing.subtotal,
                total = uiState.value.pricing.total,
                deliveryOption = _formData.value.deliveryOption,
                address = if (_formData.value.deliveryOption == "delivery") "${_formData.value.street}, ${_formData.value.comuna}, ${_formData.value.region}" else null,
                branch = _formData.value.branch,
                pickupDate = _formData.value.pickupDate,
                pickupTimeSlot = _formData.value.pickupTimeSlot,
                paymentMethod = _formData.value.paymentMethod,
                notes = _formData.value.notes,
                contactName = "${_formData.value.firstName} ${_formData.value.lastName}",
                contactEmail = _formData.value.email,
                contactPhone = _formData.value.phone,
                contactBirthDate = _formData.value.birthDate,
                discounts = null // TODO: Implement discounts display
            )

            _uiState.update { it.copy(isLoading = false, order = order) }
        }
    }
}

package com.example.pasteleriamilssaboresandroid.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.database.order.OrderRepository
import com.example.pasteleriamilssaboresandroid.data.database.user.User
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.util.computeDiscounts
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val order: com.example.pasteleriamilssaboresandroid.domain.model.Order? = null,
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

class CheckoutViewModel(private val orderRepository: OrderRepository) : ViewModel() {
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
                CheckoutFormData(couponCode = coupon)
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

        if (!com.example.pasteleriamilssaboresandroid.util.isValidRun(data.run)) errors["run"] = "RUN no válido"
        if (!com.example.pasteleriamilssaboresandroid.util.isNonEmpty(data.firstName)) errors["firstName"] = "El nombre es requerido"
        if (!com.example.pasteleriamilssaboresandroid.util.isNonEmpty(data.lastName)) errors["lastName"] = "El apellido es requerido"
        if (!com.example.pasteleriamilssaboresandroid.util.isValidEmail(data.email)) errors["email"] = "Email no válido"
        if (!com.example.pasteleriamilssaboresandroid.util.isValidChileanPhone(data.phone)) errors["phone"] = "Teléfono no válido"
        if (!com.example.pasteleriamilssaboresandroid.util.isValidBirthDate(data.birthDate)) errors["birthDate"] = "Fecha de nacimiento no válida"

        if (data.deliveryOption == "delivery") {
            if (!com.example.pasteleriamilssaboresandroid.util.isNonEmpty(data.street)) errors["street"] = "La calle es requerida"
            if (!com.example.pasteleriamilssaboresandroid.util.isNonEmpty(data.comuna)) errors["comuna"] = "La comuna es requerida"
            if (!com.example.pasteleriamilssaboresandroid.util.isNonEmpty(data.region)) errors["region"] = "La región es requerida"
        } else {
            if (!com.example.pasteleriamilssaboresandroid.util.isNonEmpty(data.branch)) errors["branch"] = "La sucursal es requerida"
            if (!com.example.pasteleriamilssaboresandroid.util.isNonEmpty(data.pickupTimeSlot)) errors["pickupTimeSlot"] = "El horario es requerido"
        }

        _uiState.update { it.copy(validationErrors = errors, isFormValid = errors.isEmpty()) }
    }

    fun processOrder(items: List<CartItem>, user: User?) {
        validateForm()
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            delay(5000) // Simulate processing

            val order = com.example.pasteleriamilssaboresandroid.data.database.order.Order(
                code = "MS-${System.currentTimeMillis().toString().takeLast(6)}",
                createdAt = System.currentTimeMillis(),
                userId = user?.id ?: 0,
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
                contactBirthDate = _formData.value.birthDate
            )

            if (user != null) {
                orderRepository.insert(order)
            }
            
            val domainOrder = com.example.pasteleriamilssaboresandroid.domain.model.Order(
                code = order.code,
                createdAt = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(order.createdAt),
                userId = order.userId.toString(),
                items = order.items,
                subtotal = order.subtotal,
                total = order.total,
                deliveryOption = order.deliveryOption,
                address = order.address,
                branch = order.branch,
                pickupDate = order.pickupDate,
                pickupTimeSlot = order.pickupTimeSlot,
                paymentMethod = order.paymentMethod,
                notes = order.notes,
                contactName = order.contactName,
                contactEmail = order.contactEmail,
                contactPhone = order.contactPhone,
                contactBirthDate = order.contactBirthDate,
                discounts = null
            )

            _uiState.update { it.copy(isLoading = false, order = domainOrder) }
        }
    }
}

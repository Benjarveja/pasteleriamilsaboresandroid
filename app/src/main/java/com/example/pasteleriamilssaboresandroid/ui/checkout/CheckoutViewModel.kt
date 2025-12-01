package com.example.pasteleriamilssaboresandroid.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleriamilssaboresandroid.data.repository.AuthRepository
import com.example.pasteleriamilssaboresandroid.data.repository.NetworkCheckoutRepository
import com.example.pasteleriamilssaboresandroid.domain.model.CartItem
import com.example.pasteleriamilssaboresandroid.domain.model.Order
import com.example.pasteleriamilssaboresandroid.util.PricingResult
import com.example.pasteleriamilssaboresandroid.util.computeDiscounts
import com.example.pasteleriamilssaboresandroid.util.isNonEmpty
import com.example.pasteleriamilssaboresandroid.util.isValidBirthDate
import com.example.pasteleriamilssaboresandroid.util.isValidChileanPhone
import com.example.pasteleriamilssaboresandroid.util.isValidEmail
import com.example.pasteleriamilssaboresandroid.util.isValidRun
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val order: Order? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val pricing: PricingResult = computeDiscounts(0, null),
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

class CheckoutViewModel(
    private val checkoutRepository: NetworkCheckoutRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _formData = MutableStateFlow(CheckoutFormData())
    val formData: StateFlow<CheckoutFormData> = _formData.asStateFlow()

    fun loadForm(subtotal: Int) {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            val coupon = _formData.value.couponCode
            _uiState.update { it.copy(pricing = computeDiscounts(subtotal, coupon.ifEmpty { null })) }

            _formData.update {
                if (user != null) {
                    it.copy(
                        run = user.run.orEmpty(),
                        firstName = user.firstName.orEmpty(),
                        lastName = user.lastName.orEmpty(),
                        email = user.email,
                        phone = user.phone.orEmpty(),
                        birthDate = user.birthDate.orEmpty(),
                        street = user.street.orEmpty(),
                        comuna = user.comuna.orEmpty(),
                        region = user.region.orEmpty()
                    )
                } else {
                    CheckoutFormData(couponCode = coupon)
                }
            }
            validateForm()
        }
    }

    fun updateFormData(newData: CheckoutFormData) {
        _formData.value = newData
        validateForm()
    }

    fun applyCoupon(subtotal: Int) {
        _uiState.update { it.copy(pricing = computeDiscounts(subtotal, _formData.value.couponCode)) }
    }

    private fun validateForm() {
        val data = _formData.value
        val errors = mutableMapOf<String, String>()

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

    fun processOrder(items: List<CartItem>) {
        validateForm()
        if (!_uiState.value.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val order = buildOrder(items)
            checkoutRepository.checkout(order).fold(
                onSuccess = { created ->
                    _uiState.update { it.copy(isLoading = false, order = created) }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = throwable.message ?: "Error al procesar pedido"
                        )
                    }
                }
            )
        }
    }

    private fun buildOrder(items: List<CartItem>): Order {
        val data = _formData.value
        return Order(
            code = "MS-${System.currentTimeMillis().toString().takeLast(6)}",
            createdAt = "",
            status = null,
            userId = null,
            items = items,
            subtotal = uiState.value.pricing.subtotal,
            total = uiState.value.pricing.total,
            deliveryOption = data.deliveryOption,
            address = if (data.deliveryOption == "delivery") listOf(
                data.street,
                data.comuna,
                data.region
            ).filter { it.isNotBlank() }.joinToString(", ") else null,
            branch = data.branch.ifBlank { null },
            pickupDate = data.pickupDate.ifBlank { null },
            pickupTimeSlot = data.pickupTimeSlot.ifBlank { null },
            paymentMethod = data.paymentMethod,
            notes = data.notes.ifBlank { null },
            contactName = "${data.firstName} ${data.lastName}".trim(),
            contactEmail = data.email,
            contactPhone = data.phone,
            contactBirthDate = data.birthDate.ifBlank { null },
            discounts = null
        )
    }
}

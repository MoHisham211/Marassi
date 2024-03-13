package mo.zain.marassi.model.paymob

data class PaymentRequest(
    val amount_cents: String,
    val auth_token: String,
    val billing_data: BillingData,
    val currency: String,
    val expiration: Int,
    val integration_id: String,
    val order_id: String
)
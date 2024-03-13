package mo.zain.marassi.model.paymob

data class OrderRequest(
    val amount_cents: String,
    val auth_token: String,
    val currency: String,
    val delivery_needed: String,
//    val items: List<Any>
)
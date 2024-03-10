package mo.zain.marassi.model

data class GetAllRequests(
    val `data`: List<DataXX>,
    val message: String,
    val success: Boolean
)
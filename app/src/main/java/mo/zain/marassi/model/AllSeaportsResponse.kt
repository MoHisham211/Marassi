package mo.zain.marassi.model

data class AllSeaportsResponse(
    val `data`: List<SeaPortItems>,
    val message: String,
    val success: Boolean
)
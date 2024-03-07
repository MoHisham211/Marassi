package mo.zain.marassi.model

import java.io.Serializable

data class SeaPortItems(
    val `data`: List<DataX>,
    val message: String,
    val success: Boolean
): Serializable
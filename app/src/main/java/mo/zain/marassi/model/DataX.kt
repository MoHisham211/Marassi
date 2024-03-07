package mo.zain.marassi.model

import java.io.Serializable

data class DataX(
    val description: String,
    val id: Int,
    val links: List<Link>,
    val location: String,
    val name: String,
    val photo: String
): Serializable
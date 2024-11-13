import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val name: String,
    val points: Double? = 0.000,
    val profileImageUrl: String? = null
)
import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("score")
    val score: String? = null,

    @field:SerializedName("profileImageUrl")
    val profileImageUrl: String? = null
)
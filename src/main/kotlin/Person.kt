import kotlinx.serialization.Serializable

@Serializable
data class Person(
    val id: Int,
    val name: String,
    val age: Int,
    val type: Type = Type.Type1
)

@Serializable
enum class Type {
    Type1,
    Type2,
    Type3,
}

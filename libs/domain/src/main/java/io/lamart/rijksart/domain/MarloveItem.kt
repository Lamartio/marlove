import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

typealias MarloveItems = List<MarloveItem>

@Serializable
data class MarloveItem(
    val _id: String,
    val confidence: Double,
    val image: String,
    val text: String,
) {
    companion object {
        fun listSerializer(): KSerializer<List<MarloveItem>> = MarloveItem
             .serializer()
             .let(::ListSerializer)
    }
}

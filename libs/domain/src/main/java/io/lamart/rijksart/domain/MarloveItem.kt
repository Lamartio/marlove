

typealias MarloveItems = List<MarloveItem>

data class MarloveItem(
    val _id: String,
    val confidence: Double,
    val image: String,
    val text: String,
)
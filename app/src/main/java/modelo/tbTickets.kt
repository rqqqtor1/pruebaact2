package modelo

data class tbTickets(
    val ticketNumber: Int,
    val title: String,
    val description: String,
    val author: String,
    val email: String,
    val creationDate: String,
    val completionDate: String?,
    val status: String
)
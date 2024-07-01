package modelo

data class tbTickets(
    val numTicket: Int,
    val titulo: String,
    val descripcion: String,
    val autor: String,
    val emailAutor: String,
    val fechaCreacion: String,
    val fechaCierre: String,
    val estado: String
)
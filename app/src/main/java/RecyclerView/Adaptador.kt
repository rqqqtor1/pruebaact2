package RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fer.orantes.actvferorantes.R
import modelo.tbTickets

class Adaptador(private val tickets: List<tbTickets>) : RecyclerView.Adapter<Adaptador.TicketViewHolder>() {


    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val numTicket: TextView = view.findViewById(R.id.tvNumTicket)
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val autor: TextView = view.findViewById(R.id.tvAutor)
        val emailAutor: TextView = view.findViewById(R.id.tvEmailAutor)
        val fechaCreacion: TextView = view.findViewById(R.id.tvFechaCreacion)
        val fechaCierre: TextView = view.findViewById(R.id.tvFechaCierre)
        val estado: TextView = view.findViewById(R.id.tvEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.numTicket.text = ticket.numTicket.toString()
        holder.titulo.text = ticket.titulo
        holder.descripcion.text = ticket.descripcion
        holder.autor.text = ticket.autor
        holder.emailAutor.text = ticket.emailAutor
        holder.fechaCreacion.text = ticket.fechaCreacion
        holder.fechaCierre.text = ticket.fechaCierre
        holder.estado.text = ticket.estado
    }

    override fun getItemCount() = tickets.size
}
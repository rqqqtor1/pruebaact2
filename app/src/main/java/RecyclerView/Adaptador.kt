package RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fer.orantes.actvferorantes.R
import modelo.tbTickets
import android.widget.EditText
import android.widget.ImageView



class Adaptador(
    private val tickets: List<tbTickets>,
    private val onEliminarTicket: (tbTickets) -> Unit,
    private val onEditarTicket: (tbTickets) -> Unit
) : RecyclerView.Adapter<Adaptador.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTituloCard: TextView = itemView.findViewById(R.id.txtTituloCard)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val btnEliminar: ImageView = view.findViewById(R.id.btnElimCard)
        val btnEditar: ImageView = view.findViewById(R.id.btnUpdateCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.txtTituloCard.text = ticket.title
        holder.txtEstado.text = ticket.status
        holder.btnEliminar.setOnClickListener { onEliminarTicket(ticket) }
        holder.btnEditar.setOnClickListener { onEditarTicket(ticket) }
    }

    override fun getItemCount() = tickets.size
}
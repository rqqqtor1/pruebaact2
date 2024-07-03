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
        val btnEliminar: Button = view.findViewById(R.id.btnUpdateCard)
        val btnEditar: Button = view.findViewById(R.id.btnElimCard)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.btnEliminar.setOnClickListener { onEliminarTicket(ticket) }
        holder.btnEditar.setOnClickListener { onEditarTicket(ticket) }

    }

    override fun getItemCount() = tickets.size
}
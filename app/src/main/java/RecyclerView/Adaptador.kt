package RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fer.orantes.actvferorantes.R
import modelo.tbTickets
import android.widget.EditText
import android.widget.ImageView




class Adaptador(private val tickets: List<tbTickets>) : RecyclerView.Adapter<Adaptador.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtTituloCard)
        val estado: EditText = view.findViewById(R.id.txtEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.titulo.text = ticket.title
        holder.estado.setText(ticket.status)
    }

    override fun getItemCount() = tickets.size
}
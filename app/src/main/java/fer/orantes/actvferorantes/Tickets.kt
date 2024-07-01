package fer.orantes.actvferorantes

import RecyclerView.Adaptador
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.tbTickets

class Tickets : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)
        rcvTickets.layoutManager = LinearLayoutManager(this)

        fun obtenerTickets(): List<tbTickets> {
            val objConexion = ClaseConexion().cadenaConexion()
            val traerTickets = objConexion?.prepareStatement("SELECT * FROM ac_tickets WHERE email_autor = ? ORDER BY num_ticket DESC")!!
            traerTickets.setString(1, email)
            val resultSet = traerTickets.executeQuery()
            val listaTickets = mutableListOf<tbTickets>()
            while (resultSet.next()) {
                val numTicket = resultSet.getInt("NUM_TICKET")
                val titulo = resultSet.getString("TITULO")
                val descripcion = resultSet.getString("DESCRIPCION")
                val autor = resultSet.getString("AUTOR")
                val emailAutor = resultSet.getString("EMAIL_AUTOR")
                val fechaCreacion = resultSet.getString("FECHA_CREACION")
                val fechaCierre = resultSet.getString("FECHA_CIERRE")
                val estado = resultSet.getString("ESTADO")

                val ticket = tbTickets(numTicket, titulo, descripcion, autor, emailAutor, fechaCreacion, fechaCierre, estado)
                listaTickets.add(ticket)
            }
            return listaTickets
        }

        CoroutineScope(Dispatchers.IO).launch {
            val ticketsDB = obtenerTickets()
            withContext(Dispatchers.Main) {
                val adapter = Adaptador(ticketsDB)
                rcvTickets.adapter = adapter
            }
        }
    }
}
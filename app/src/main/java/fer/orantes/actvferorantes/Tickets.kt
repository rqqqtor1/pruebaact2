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
import android.widget.Toast
import kotlinx.coroutines.*
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException



class Tickets : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: Adaptador
    private val tickets = mutableListOf<tbTickets>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        recyclerView = findViewById(R.id.rcvTickets)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adaptador = Adaptador(tickets)
        recyclerView.adapter = adaptador

        obtenerTickets()
        // Ejemplo de cómo insertar un ticket
        insertarTicket(tbTickets(1, "Título de ejemplo", "Descripción de ejemplo", "Autor de ejemplo", "email@ejemplo.com", "2024-07-01", "2024-07-02", "Activo"))
    }

    private fun obtenerTickets() {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                try {
                    val statement = conexion.createStatement()
                    val resultSet: ResultSet = statement.executeQuery("SELECT * FROM tickets")

                    while (resultSet.next()) {
                        val ticket = tbTickets(
                            resultSet.getInt("ticket_number"),
                            resultSet.getString("title"),
                            resultSet.getString("descripcion"),
                            resultSet.getString("author"),
                            resultSet.getString("email"),
                            resultSet.getString("creation_date"),
                            resultSet.getString("completion_date"),
                            resultSet.getString("status")
                        )
                        tickets.add(ticket)
                    }

                    withContext(Dispatchers.Main) {
                        adaptador.notifyDataSetChanged()
                    }

                    resultSet.close()
                    statement.close()
                    conexion.close()
                } catch (e: SQLException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Tickets, "Error al obtener los tickets: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Tickets, "Error al conectar con la base de datos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun insertarTicket(ticket: tbTickets) {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                try {
                    val preparedStatement = conexion.prepareStatement(
                        "INSERT INTO tickets (ticket_number, title, descripcion, author, email, creation_date, completion_date, status) VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?)"
                    )
                    preparedStatement.setInt(1, ticket.ticketNumber)
                    preparedStatement.setString(2, ticket.title)
                    preparedStatement.setString(3, ticket.descripcion)
                    preparedStatement.setString(4, ticket.author)
                    preparedStatement.setString(5, ticket.email)
                    preparedStatement.setString(6, ticket.creationDate)
                    preparedStatement.setString(7, ticket.completionDate)
                    preparedStatement.setString(8, ticket.status)

                    val rowsInserted = preparedStatement.executeUpdate()
                    if (rowsInserted > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Tickets, "Ticket insertado exitosamente", Toast.LENGTH_LONG).show()
                            tickets.add(ticket)
                            adaptador.notifyDataSetChanged()
                        }
                    }

                    preparedStatement.close()
                    conexion.close()
                } catch (e: SQLException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Tickets, "Error al insertar el ticket: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Tickets, "Error al conectar con la base de datos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
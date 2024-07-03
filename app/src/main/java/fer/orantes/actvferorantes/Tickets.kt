package fer.orantes.actvferorantes

import RecyclerView.Adaptador
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*
import java.sql.DriverManager
import java.sql.PreparedStatement
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
        adaptador = Adaptador(tickets, ::eliminarTicket, ::mostrarDialogoEditarTicket)
        recyclerView.adapter = adaptador

        obtenerTickets()

        val btnAddTicket: Button = findViewById(R.id.btnAddTicket)
        btnAddTicket.setOnClickListener {
            mostrarDialogoAgregarTicket()
        }
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
                            resultSet.getString("description"),
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

    private fun mostrarDialogoAgregarTicket() {
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.dialog_tickets, null)

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcion)
        val etAuthor = view.findViewById<EditText>(R.id.etAuthor)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etCompletionDate = view.findViewById<EditText>(R.id.etCompletionDate)
        val btnSubmitTicket = view.findViewById<Button>(R.id.btnSubmitTicket)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        btnSubmitTicket.setOnClickListener {
            val title = etTitle.text.toString()
            val descripcion = etDescripcion.text.toString()
            val author = etAuthor.text.toString()
            val email = etEmail.text.toString()
            val completionDate = etCompletionDate.text.toString()

            val newTicket = tbTickets(0, title, descripcion, author, email, "2024-07-01", completionDate, "Activo")

            insertarTicket(newTicket)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun mostrarDialogoEditarTicket(ticket: tbTickets) {
        val inflater = LayoutInflater.from(this)
        val view: View = inflater.inflate(R.layout.dialog_tickets, null)

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescripcion = view.findViewById<EditText>(R.id.etDescripcion)
        val etAuthor = view.findViewById<EditText>(R.id.etAuthor)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etCompletionDate = view.findViewById<EditText>(R.id.etCompletionDate)
        val btnSubmitTicket = view.findViewById<Button>(R.id.btnSubmitTicket)

        etTitle.setText(ticket.title)
        etDescripcion.setText(ticket.description)
        etAuthor.setText(ticket.author)
        etEmail.setText(ticket.email)
        etCompletionDate.setText(ticket.completionDate)

        btnSubmitTicket.text = "Actualizar"

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create()

        btnSubmitTicket.setOnClickListener {
            val updatedTicket = tbTickets(
                ticket.ticketNumber,
                etTitle.text.toString(),
                etDescripcion.text.toString(),
                etAuthor.text.toString(),
                etEmail.text.toString(),
                ticket.creationDate,
                etCompletionDate.text.toString(),
                ticket.status
            )

            actualizarTicket(updatedTicket)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun insertarTicket(ticket: tbTickets) {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                try {
                    // Generar un número de ticket único
                    val statement = conexion.createStatement()
                    val resultSet: ResultSet = statement.executeQuery("SELECT MAX(ticket_number) AS max_ticket_number FROM tickets")
                    var maxTicketNumber = 0
                    if (resultSet.next()) {
                        maxTicketNumber = resultSet.getInt("max_ticket_number")
                    }
                    val newTicketNumber = maxTicketNumber + 1
                    resultSet.close()
                    statement.close()

                    val preparedStatement = conexion.prepareStatement(
                        "INSERT INTO tickets (ticket_number, title, description, author, email, creation_date, completion_date, status) VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD'), ?)"
                    )
                    preparedStatement.setInt(1, newTicketNumber)
                    preparedStatement.setString(2, ticket.title)
                    preparedStatement.setString(3, ticket.description)
                    preparedStatement.setString(4, ticket.author)
                    preparedStatement.setString(5, ticket.email)
                    preparedStatement.setString(6, ticket.creationDate)
                    preparedStatement.setString(7, ticket.completionDate)
                    preparedStatement.setString(8, ticket.status)

                    val rowsInserted = preparedStatement.executeUpdate()
                    if (rowsInserted > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Tickets, "Ticket insertado exitosamente", Toast.LENGTH_LONG).show()
                            tickets.add(ticket.copy(ticketNumber = newTicketNumber))
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

    private fun actualizarTicket(ticket: tbTickets) {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                try {
                    val preparedStatement = conexion.prepareStatement(
                        "UPDATE tickets SET title = ?, description = ?, author = ?, email = ?, completion_date = TO_DATE(?, 'YYYY-MM-DD'), status = ? WHERE ticket_number = ?"
                    )
                    preparedStatement.setString(1, ticket.title)
                    preparedStatement.setString(2, ticket.description)
                    preparedStatement.setString(3, ticket.author)
                    preparedStatement.setString(4, ticket.email)
                    preparedStatement.setString(5, ticket.completionDate)
                    preparedStatement.setString(6, ticket.status)
                    preparedStatement.setInt(7, ticket.ticketNumber)

                    val rowsUpdated = preparedStatement.executeUpdate()
                    if (rowsUpdated > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Tickets, "Ticket actualizado exitosamente", Toast.LENGTH_LONG).show()
                            val index = tickets.indexOfFirst { it.ticketNumber == ticket.ticketNumber }
                            if (index != -1) {
                                tickets[index] = ticket
                                adaptador.notifyItemChanged(index)
                            }
                        }
                    }

                    preparedStatement.close()
                    conexion.close()
                } catch (e: SQLException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Tickets, "Error al actualizar el ticket: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Tickets, "Error al conectar con la base de datos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun eliminarTicket(ticket: tbTickets) {
        CoroutineScope(Dispatchers.IO).launch {
            val conexion = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                try {
                    val preparedStatement: PreparedStatement = conexion.prepareStatement(
                        "DELETE FROM tickets WHERE ticket_number = ?"
                    )
                    preparedStatement.setInt(1, ticket.ticketNumber)

                    val rowsDeleted = preparedStatement.executeUpdate()
                    if (rowsDeleted > 0) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Tickets, "Ticket eliminado exitosamente", Toast.LENGTH_LONG).show()
                            tickets.remove(ticket)
                            adaptador.notifyDataSetChanged()
                        }
                    }

                    preparedStatement.close()
                    conexion.close()
                } catch (e: SQLException) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Tickets, "Error al eliminar el ticket: ${e.message}", Toast.LENGTH_LONG).show()
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
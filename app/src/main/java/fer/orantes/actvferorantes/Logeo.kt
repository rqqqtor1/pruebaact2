package fer.orantes.actvferorantes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion

class Logeo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logeo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLogeo = findViewById<Button>(R.id.btnLogeo)
        btnLogeo.setOnClickListener {
            val username = findViewById<EditText>(R.id.etUsernamereg).text.toString()
            val password = findViewById<EditText>(R.id.txtPasswordreg).text.toString()

            // Validar las credenciales en la base de datos
            GlobalScope.launch(Dispatchers.IO) {

                val objConexion = ClaseConexion().cadenaConexion()
                val consultaUser = objConexion?.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
                )!!
                consultaUser.setString(1, username)
                consultaUser.setString(2, password)
                val resultSet = consultaUser.executeQuery()

                withContext(Dispatchers.Main) {
                    if (resultSet.next()) {
                        Toast.makeText(this@Logeo, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Logeo, Tickets::class.java)
                        intent.putExtra("email", resultSet.getString("email"))
                        intent.putExtra("password", password)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@Logeo, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



}
package fer.orantes.actvferorantes

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

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCorreoReg = findViewById<EditText>(R.id.txtEmailreg)
        val txtPasswordReg = findViewById<EditText>(R.id.txtPasswordreg)
        val txtUser = findViewById<EditText>(R.id.etUsernamereg)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
GlobalScope.launch(Dispatchers.IO){
val objConexion = ClaseConexion().cadenaConexion()

    val crearUser =
        objConexion?.prepareStatement("INSERT INTO users (username, email, passwordsita) values (?, ?, ?)")!!

crearUser.setString(1, txtCorreoReg.text.toString())
    crearUser.setString(2, txtPasswordReg.text.toString())
    crearUser.setString(3, txtUser.text.toString())
crearUser.executeQuery()
    withContext(Dispatchers.Main) {
Toast.makeText( this@Registro, "Usuario creado", Toast.LENGTH_SHORT).show()
        txtCorreoReg.setText("")
        txtPasswordReg.setText("")
        txtUser.setText("")
    }


}
        }







    }
}
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
import android.text.InputType
import android.widget.ImageView
import java.util.UUID
import android.widget.TextView





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
        val txtConfirmarPassword = findViewById<EditText>(R.id.txtConfirmarPasswordreg)
        val txtUser = findViewById<EditText>(R.id.etUsernamereg)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnRegresarLogin = findViewById<Button>(R.id.btnRegresarLogin)
        val imgVerPassword = findViewById<ImageView>(R.id.imgVerPassword)
        val imgVerConfirmacionPassword = findViewById<ImageView>(R.id.imgVerConfirmacionPassword)


        btnRegister.setOnClickListener {
            if (txtPasswordReg.text.toString() == txtConfirmarPassword.text.toString()) {
                GlobalScope.launch(Dispatchers.IO) {
                    val objConexion = ClaseConexion().cadenaConexion()
                    val crearUser = objConexion?.prepareStatement(
                        "INSERT INTO users (username, email, password) VALUES (?, ?, ?)"
                    )!!
                    crearUser.setString(1, txtUser.text.toString())
                    crearUser.setString(2, txtCorreoReg.text.toString())
                    crearUser.setString(3, txtPasswordReg.text.toString())
                    crearUser.executeUpdate()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@Registro, "Usuario creado", Toast.LENGTH_SHORT).show()
                        txtCorreoReg.setText("")
                        txtPasswordReg.setText("")
                        txtConfirmarPassword.setText("")
                        txtUser.setText("")
                    }
                }
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }
        }


        imgVerPassword.setOnClickListener {
            if (txtPasswordReg.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtPasswordReg.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtPasswordReg.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        imgVerConfirmacionPassword.setOnClickListener {
            if (txtConfirmarPassword.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                txtConfirmarPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                txtConfirmarPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }


        btnRegresarLogin.setOnClickListener {
            finish()
        }
    }
}
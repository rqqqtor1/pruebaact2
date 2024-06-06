package fer.orantes.actvferorantes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import modelo.DatabaseHelper
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class Logeo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logeo)

       val etCorreo : EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etCorreo.text.toString()
            val password = etPassword.text.toString()

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        val connection: Connection? = DatabaseHelper.getConnection()
        if (connection != null) {
            val sql = "SELECT * FROM users WHERE email = ? AND password = ?"
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setString(1, email)
            statement.setString(2, password)

            val resultSet: ResultSet = statement.executeQuery()
            if (resultSet.next()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }

            resultSet.close()
            statement.close()
            connection.close()
        }
    }
}
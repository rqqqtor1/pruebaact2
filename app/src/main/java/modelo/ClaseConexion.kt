package modelo
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ClaseConexion {

    fun cadenaConexion(): Connection?{
        try {
            val ip = "jdbc:oracle:thin:@192.168.56.1:1521:xe"
            val usuario = "SYSTEM"
            val contrasena = "desarrollo"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion
        }catch (e: Exception){
            println("Este es el error: $e")
            return null
        }
    }

}
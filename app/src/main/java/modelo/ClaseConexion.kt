package modelo
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseHelper {
    private const val URL = "jdbc:oracle:thin:@//[HOST]:[PORT]/[SERVICE_NAME]"
    private const val USER = "usuario"
    private const val PASSWORD = "contraseña"

    init {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}
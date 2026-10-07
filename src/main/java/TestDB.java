import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/gestion_eventos_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC";
        try {
            System.out.println("Connecting...");
            Connection conn = DriverManager.getConnection(url, "root", "");
            System.out.println("Connected!");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW DATABASES;");
            while(rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

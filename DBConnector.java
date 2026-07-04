import java.sql.*;

public class DBConnector {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/online_bank";
        String user = "root";
        String password = "Shruti@2005";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}

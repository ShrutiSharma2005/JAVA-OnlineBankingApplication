import java.sql.*;

public class DBConnector {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/online_bank";
        String user = "root";
        String password = "***";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }
}

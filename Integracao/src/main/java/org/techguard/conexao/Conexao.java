package org.techguard.conexao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String url = "jdbc:mysql://localhost:3306/techguard";
    private static final String user = "root";
    private static final String password = "pedro123";

    public static Connection conn;

    public static Connection getConexao() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(url, user, password);
                return conn;
            } else {
                return conn;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

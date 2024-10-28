package org.techguard.conexao;
import org.techguard.conexao.ErroNaConexaoBanco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    // Classes privadas, estáticas e "final" são classes que são usadas como cópia em outra classe
    // (sendo que todas precisam da conexão) e são imutáveis
    // Classes de url do banco, nome e senha do usuário
    private static final String url = "jdbc:mysql://TechGuardDB/techguard";
    private static final String user = "root";
    private static final String password = "solutions";

    // Objeto de conexão
    public static Connection conn;

    // Classe que funciona puxando a conexão do banco
    // Basicamente tenta fazer a conexão, caso não consiga, retorna um erro
    public static Connection getConexao() {
        try {
            if (conn == null) {
                conn = DriverManager.getConnection(url, user, password);
                return conn;
            } else {
                return conn;
            }
        } catch (SQLException e) {
            ErroNaConexaoBanco falhaConexao = new ErroNaConexaoBanco();
            falhaConexao.mostrarFalhaNaConexao();
        } finally{
            System.out.println("A operação de tentativa de conexão com o banco foi finalizada!");
        }
        return null;
    }


}

package org.techguard.conexao;
import java.sql.SQLException;

public class ErroNaConexaoBanco extends SQLException {
    public void mostrarFalhaNaConexao(){
        System.out.println("Erro ao estabelecer conexão com o banco de dados");
    }
}

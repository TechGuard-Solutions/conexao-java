package org.techguard.dao;

import org.techguard.tabelas.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.techguard.conexao.Conexao;

public class UsuarioDao {
    // Metódo de insert de dados no banco
    public void cadastrarUsuario(Usuario usuario) {
        // String que contém uma string do comando sql
        String sql = "INSERT INTO USUARIO (idUsuario, nomeUsuario, senhaUsuario, cpf, emailUsuario, telUsuario, fkEmpresa, fkTipoUsuario) VALUES (?, ? , ?, ?, ?, ?, ?, ?)";

        // O PreparedStatement é uma interface em Java usada para executar instruções SQL pré-compiladas, tornando o acesso ao banco de dados mais eficiente e seguro.
        PreparedStatement ps = null;

        // try catch para fazer os inserts
        try {
            // Solicitando a conexão com o banco
            ps = Conexao.getConexao().prepareStatement(sql);
            // Setando os inserts conforme os atributos do banco
            // Set + Tipagem do dados
            // Indicie conforme o script do isert + get do objeto que deseja inserir
            ps.setInt(1, usuario.getIdUsuario());
            ps.setString(2, usuario.getNomeUsuario());
            ps.setString(3, usuario.getSenhaUsuario());
            ps.setString(4, usuario.getCpf());
            ps.setString(5, usuario.getEmailUsuario());
            ps.setString(6, usuario.getTelUsuario());
            ps.setInt(7, usuario.getFkEmpresa());
            ps.setInt(8, usuario.getFkTipoUsuario());

            // Executando o insert e fechando o prepared statement
            ps.execute();
            ps.close();
            // Caso de um erro de exception sql, vai retornar um erro runtime com a exceção junto
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Metodo de select no banco
    public void selectUsuario() {
        // String que contém uma string do comando sql
        String sql = "SELECT * FROM USUARIO";

        // Prepared statement que ja foi explicado anteriormente, que sempre deve ser iniciado nem que seja nulo
        // Result set é uma interface que consegue executar querys no próprio terminal
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Result set deve referenciar o prepared statement e executar a query pedida
            ps = Conexao.getConexao().prepareStatement(sql);
            rs = ps.executeQuery();

            // Enquanto o result set tiver uma query nova pra ser exibida, irá executar o bloco while
            while (rs.next()) {
            // Dados exibidos atraves do sout no terminal, dando get nos objetos juntamente do nome do atributo da tabela de como está no banco
                System.out.println("--------------------------");
                System.out.println("ID: " + rs.getInt("idUsuario"));
                System.out.println("Nome: " + rs.getString("nomeUsuario"));
                System.out.println("CPF: " + rs.getString("cpf"));
                System.out.println("Email: " + rs.getString("emailUsuario"));
                System.out.println("Telefone: " + rs.getString("telUsuario"));
                System.out.println("Empresa: " + rs.getInt("fkEmpresa"));
                System.out.println("Tipo Usuário: " + rs.getInt("fkTipoUsuario"));
                System.out.println("--------------------------");
            }


            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

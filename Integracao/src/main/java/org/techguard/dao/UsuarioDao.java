package org.techguard.dao;
import org.techguard.tabelas.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.techguard.conexao.Conexao;

public class UsuarioDao {
    public void cadastrarUsuario(Usuario usuario){
        String sql = "INSERT INTO USUARIO (idUsuario, nomeUsuario, senhaUsuario, cpf, emailUsuario, telUsuario, fkEmpresa, fkTipoUsuario) VALUES (?, ? , ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = null;

        try {
            ps = Conexao.getConexao().prepareStatement(sql);
            ps.setInt(1, usuario.getIdUsuario());
            ps.setString(2, usuario.getNomeUsuario());
            ps.setString(3, usuario.getSenhaUsuario());
            ps.setString(4, usuario.getCpf());
            ps.setString(5, usuario.getEmailUsuario());
            ps.setString(6, usuario.getTelUsuario());
            ps.setInt(7, usuario.getFkEmpresa());
            ps.setInt(8, usuario.getFkTipoUsuario());

            ps.execute();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void selectUsuario(){
        String sql = "SELECT * FROM USUARIO";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = Conexao.getConexao().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
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

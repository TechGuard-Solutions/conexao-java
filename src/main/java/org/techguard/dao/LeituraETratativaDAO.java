package org.techguard.dao;

import org.techguard.conexao.ErroNoInsert;
import org.techguard.conexao.ErroNoSelect;

import org.techguard.tabelas.Usuario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.techguard.conexao.Conexao;
import org.techguard.tratativa.LeituraETratativa;

import static org.techguard.tratativa.LeituraETratativa.*;

public class LeituraETratativaDAO {

    public void cadastrarDados(LeituraETratativa leituraETratativa){
        String sql = "INSERT INTO registros (data, nome, attack_ou_disclosure, modificados_affect, modificados_downstream_target, modificados_impact) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;

        try {

            ps = Conexao.getConexao().prepareStatement(sql);
            for (int i = 0; i < 5; i++) {
                ps.setString(1, listaDatas.get(i));
                ps.setString(2, listaNomes.get(i));
                ps.setString(3, listaAttackOuDisclosure.get(i));
                ps.setString(4, listaModificadosAffect.get(i));
                ps.setString(5, listaModificadosDownstreamTarget.get(i));
                ps.setString(6, listaModificadosImpact.get(i));
                ps.executeUpdate();
            }

            // Executando o insert e fechando o prepared statement
            ps.execute();
//            ps.close();
            // Caso de um erro de exception sql, vai retornar um erro runtime com a exceção junto
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
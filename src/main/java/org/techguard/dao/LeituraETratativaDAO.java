package org.techguard.dao;

import org.techguard.modelo.Incidente;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.techguard.conexao.Conexao;

public class LeituraETratativaDAO {

    public void cadastrarDados(List<Incidente> incidentes) {
        String sql = "INSERT INTO registros (data, nome, attack_ou_disclosure, modificados_affect, modificados_downstream_target, modificados_impact) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = Conexao.getConexao().prepareStatement(sql)) {
            for (Incidente incidente : incidentes) {
                ps.setDate(1, new java.sql.Date(incidente.getData().getTime())); // Converta para java.sql.Date
                ps.setString(2, incidente.getNome());
                ps.setString(3, incidente.getTipoIncidente());
                ps.setString(4, incidente.getAffected());
                ps.setString(5, incidente.getDownstreamTarget());
                ps.setString(6, incidente.getImpact());
                ps.executeUpdate();
            }

            // Caso de um erro de exception sql, vai retornar um erro runtime com a exceção junto
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
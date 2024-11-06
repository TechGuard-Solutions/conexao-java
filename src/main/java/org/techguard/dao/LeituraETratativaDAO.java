package org.techguard.dao;

import org.techguard.conexao.ErroNaConexaoBanco;
import org.techguard.modelo.Incidente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.techguard.conexao.Conexao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeituraETratativaDAO {
    private static final Logger LOGGER = LogManager.getLogger(LeituraETratativaDAO.class);

    public void cadastrarDados(List<Incidente> incidentes) {
        LOGGER.info("Iniciando a persistência dos dados no banco de dados.");

        String sql = "INSERT INTO registros (data, nome, attack_ou_disclosure, modificados_affect, modificados_downstream_target, modificados_impact) VALUES (?, ?, ?, ?, ?, ?)";
        String truncate = "TRUNCATE TABLE registros";

        try (Connection conexao = Conexao.getConexao(); // try-with-resources para a Connection
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            LOGGER.info("Conexão com o banco de dados estabelecida com sucesso. URL: {}", conexao.getMetaData().getURL());

            PreparedStatement psTruncate = conexao.prepareStatement(truncate);
            psTruncate.executeUpdate();

            for (Incidente incidente : incidentes) {
                ps.setDate(1, new java.sql.Date(incidente.getData().getTime())); // Converta para java.sql.Date
                ps.setString(2, incidente.getNome());
                ps.setString(3, incidente.getTipoIncidente());
                ps.setString(4, incidente.getAffected());
                ps.setString(5, incidente.getDownstreamTarget());
                ps.setString(6, incidente.getImpact());
                ps.executeUpdate();
                LOGGER.debug("Incidente inserido no banco de dados: {}", incidente);

            }

            ps.close();
            // Caso de um erro de exception sql, vai retornar um erro runtime com a exceção junto
        } catch (SQLException e) {
            LOGGER.error("Erro ao inserir dados no banco de dados: " + e.getMessage(), e);
            throw new RuntimeException("Erro ao persistir dados no banco de dados", e);
        } catch (ErroNaConexaoBanco e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Persistência dos dados no banco de dados concluída.");
    }
}
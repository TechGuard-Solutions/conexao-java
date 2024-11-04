package org.techguard;

import org.techguard.api.ClassificadorAPI;
import org.techguard.conexao.S3Connection;
import org.techguard.dao.LeituraETratativaDAO;
import org.techguard.modelo.Incidente;
import org.techguard.tratativa.ProcessadorDados;
import software.amazon.awssdk.regions.Region;

import java.io.IOException;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        String bucketName = "techguard-bucket";
        String key = "basededados.xlsx";
        String apiKey = "AIzaSyDlti8js0JJDsZDviQ9bbUOzN6P2YXzUtA";
        Region region = Region.US_EAST_1;

        try (S3Connection s3Connection = new S3Connection(bucketName, key, region)) {
            ClassificadorAPI classificadorAPI = new ClassificadorAPI(apiKey);
            ProcessadorDados processador = new ProcessadorDados(s3Connection, classificadorAPI);

            List<Incidente> incidentes = processador.processar();

            LeituraETratativaDAO dao = new LeituraETratativaDAO();
            dao.cadastrarDados(incidentes);

        } catch (IOException e) {
            System.err.println("Erro ao processar dados: " + e.getMessage());
        }
    }
}
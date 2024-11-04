package org.techguard.tratativa;

import org.apache.poi.ss.usermodel.Workbook;
import org.techguard.api.ClassificadorAPI;
import org.techguard.conexao.S3Connection;
import org.techguard.modelo.Incidente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessadorDados {
    private S3Connection s3Connection;
    private ClassificadorAPI classificadorAPI;
    private TratadorDadosIncidentes tratadorDadosIncidentes;
    private TratadorDadosClassificacao tratadorDadosClassificacao;


    public ProcessadorDados(S3Connection s3Connection, ClassificadorAPI classificadorAPI) {
        this.s3Connection = s3Connection;
        this.classificadorAPI = classificadorAPI;
        this.tratadorDadosIncidentes = new TratadorDadosIncidentes(s3Connection, classificadorAPI);
        this.tratadorDadosClassificacao = new TratadorDadosClassificacao(s3Connection, classificadorAPI);
    }


    public List<Incidente> processar() throws IOException {

        try (Workbook workbook = s3Connection.getWorkbook()) {
            List<Incidente> incidentes = tratadorDadosIncidentes.processarDados(workbook);
            incidentes = tratadorDadosClassificacao.processarDados(workbook);
            return incidentes;
        }
    }
}
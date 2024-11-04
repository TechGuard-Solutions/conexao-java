package org.techguard.tratativa;


import org.apache.poi.ss.usermodel.*;
import org.techguard.api.ClassificadorAPI;
import org.techguard.conexao.S3Connection;
import org.techguard.modelo.Incidente;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TratadorDadosIncidentes extends TratadorDados {

    public TratadorDadosIncidentes(S3Connection s3Connection, ClassificadorAPI classificadorAPI) {
        super(s3Connection, classificadorAPI);
    }

    @Override
    public List<Incidente> processarDados(Workbook workbook) throws IOException {
        return processarDados(workbook, new ArrayList<>()); // Chama o outro método com uma nova lista
    }

    @Override
    public List<Incidente> processarDados(Workbook workbook, List<Incidente> incidentes) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);

            for (int rowIndex = 1; rowIndex <= 50; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Incidente incidente = new Incidente();

                    try {
                        Cell cellData = row.getCell(0);
                        if (cellData != null && cellData.getCellType() == CellType.NUMERIC) {
                            Date data = cellData.getDateCellValue();
                            incidente.setData(data);
                        }

                        Cell cellNome = row.getCell(1);
                        if (cellNome != null && cellNome.getCellType() == CellType.STRING) {
                            String nome = cellNome.getStringCellValue();
                            incidente.setNome(nome);
                        }

                        Cell cellTipo = row.getCell(2);
                        if (cellTipo != null && cellTipo.getCellType() == CellType.STRING) {
                            String tipo = cellTipo.getStringCellValue();
                            incidente.setTipoIncidente(tipo);
                        }

                        incidentes.add(incidente);

                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha " + rowIndex + ": " + e.getMessage());
                    }
                }
            }
        }
        return incidentes;
    }
}
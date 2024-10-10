import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class LeituraBucket {

    public static void main(String[] args) {
        TratamentoAffectedCode affect = new TratamentoAffectedCode();
        affect.tratandoDados();
        TratamentoImpacto.tratandoDados();
        mostrandoTabela();

    }

    public static void mostrandoTabela() {
        String bucketName = "s3-sprint";
        String key = "basededados.xlsx"; // Altere para a chave do seu arquivo no bucket
        Region region = Region.US_EAST_1; // Substitua pela região do seu bucket

        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (InputStream inputStream = s3.getObject(getObjectRequest);
             Workbook workbook = new XSSFWorkbook(inputStream)) { // Usando Apache POI para ler o arquivo XLSX
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("Folha: " + sheet.getSheetName()); // Nome da folha

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (cell.getColumnIndex() != 0 && cell.getColumnIndex() != 1 &&
                                cell.getColumnIndex() != 2 && cell.getColumnIndex() != 5 &&
                                cell.getColumnIndex() != 7 && cell.getColumnIndex() != 49) {
                            continue;
                        }
                        if (cell.getCellType() == CellType.STRING &&
                                (cell.getStringCellValue().toUpperCase().contains("UNKNOWN") ||
                                        cell.getStringCellValue().toUpperCase().contains("N/A"))) {
                            continue;
                        }


                        switch (cell.getCellType()) {
                            case STRING:
                                if (cell.getColumnIndex() == 5) { // Verifica se é a coluna de índice 5
                                    if (cell.getRowIndex() == 0) {
                                        System.out.print("Affected Code \t");
                                    } else {

                                        String term = cell.getStringCellValue();
                                        String category = TratamentoAffectedCode.alterandoDados(term);
                                        // Exibe a classificação tratada
                                        System.out.print(TratamentoAffectedCode.listaModificados.get(TratamentoAffectedCode.listaModificados.size() - 1) + "\t");
                                    }
                                } else if (cell.getColumnIndex() == 49) {
                                    if (cell.getRowIndex() == 0) {
                                        System.out.print("Impact \t");
                                    } else {

                                        String term2 = cell.getStringCellValue();
                                        String category = TratamentoImpacto.alterandoDados(term2);
                                        // Exibe a classificação tratada
                                        System.out.print(TratamentoImpacto.listaModificadosImpact.get(TratamentoImpacto.listaModificadosImpact.size() - 1) + "\t");
                                    }
                                } else {
                                    System.out.print(cell.getStringCellValue() + "; \t");
                                }
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    Date date = cell.getDateCellValue();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    System.out.print(dateFormat.format(date) + " | ");
                                } else {
                                    System.out.print(cell.getNumericCellValue() + "; ");
                                }
                                break;
                            case BOOLEAN:
                                System.out.print(cell.getBooleanCellValue() + "; ");
                                break;
                            case BLANK:
                                System.out.print("Blank; ");
                                break;
                            case ERROR:
                                System.out.print("Error; ");
                                break;
                            default:
                                System.out.print("Unknown type");
                        }
                    }
                    System.out.println(); // Pula para a próxima linha
                }
                System.out.println(); // Adiciona uma linha em branco entre as folhas
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (s3 != null) {
                s3.close();
            }
        }
    }


}
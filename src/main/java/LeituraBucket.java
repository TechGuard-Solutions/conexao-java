import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class LeituraBucket {
    public static void main(String[] args) {
        String bucketName = "techguard-bucket";
        String key = "basededados.xlsx"; // Altere para a chave(nome do seu arquivo no bucket) do seu arquivo
        Region region = Region.US_EAST_1; // Substitua pela sua região do bucket

        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        // Construção de um novo objeto para solicitar o arquivo específico de um bucket no Amazon S3.
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Lendo o conteúdo do arquivo diretamente do S3
        try (InputStream inputStream = s3.getObject(getObjectRequest);
             Workbook workbook = new XSSFWorkbook(inputStream)) { // Usando Apache POI para ler o arquivo XLSX

            // Itera através de todas as folhas do workbook
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("Folha: " + sheet.getSheetName()); // Nome da folha

                // Itera pelas linhas e células da folha
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING:
                                System.out.print(cell.getStringCellValue() + "\t");
                                break;
                            case NUMERIC:
                                System.out.print(cell.getNumericCellValue() + "\t");
                                break;
                            case BOOLEAN:
                                System.out.print(cell.getBooleanCellValue() + "\t");
                                break;
                            default:
                                System.out.print("Unknown type\t");
                        }
                    }
                    System.out.println();
                }
                System.out.println(); // Adiciona uma linha em branco entre as folhas
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Feche o cliente S3
            s3.close();
        }
    }
}

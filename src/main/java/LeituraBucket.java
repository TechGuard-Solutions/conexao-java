import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeituraBucket {

    public static void main(String[] args) {
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
                    Cell cell = row.getCell(5); // Pega a célula do índice 5
                    if (cell != null && cell.getCellType() == CellType.STRING) {
                        String term = cell.getStringCellValue();
                        String category = classifyTermWithGemini(term);
                        System.out.println("Term: " + term + " | Category: " + category);

                    }
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

    private static String classifyTermWithGemini(String term) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyB8ockrzlb0PdYnkkm-AfKqSRrgQ6B0bRg"; // Update with your actual API key
        String jsonInputString = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"Classify the term: '%s' into one of the following categories:\\n1. Software and Applications\\n2. Malware and Vulnerabilities\\n3. Frameworks and Libraries\\n4. Hardware and Firmware\\n5. Protocols and APIs\\n6. Development Tools and Packages\\nRespond with the category name only.\"}]}]}",
                term
        );

        int attempts = 0;
        while (attempts < 10) { // Maximum 5 attempts
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Send request
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Check response code
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response
                    StringBuilder response = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                    }
                    Thread.sleep(500); // Wait for 500 milliseconds between requests
                    String classification = response.toString();
                    System.out.println(term + " -> " + classification);
                    return classification;
                } else if (responseCode == 429) { // Handling rate limiting
                    // Handle rate limiting
                    System.out.println("Error: " + responseCode + " - Too Many Requests. Retrying...");
                    Thread.sleep((long) Math.pow(2, attempts) * 1000); // Exponential backoff
                    attempts++;
                } else {
                    System.out.println("Error: " + responseCode + " - " + conn.getResponseMessage());
                    break; // Exit loop for other errors
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break; // Exit loop on exception
            }
        }
        return "Classification failed"; // In case of an error
    }
}

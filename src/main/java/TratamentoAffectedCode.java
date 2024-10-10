import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TratamentoAffectedCode {
    public static void main(String[] args) {
        tratandoDados();
    }

    public static void tratandoDados() {
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

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        try (InputStream inputStream = s3.getObject(getObjectRequest);
             Workbook workbook = new XSSFWorkbook(inputStream)) { // Usando Apache POI para ler o arquivo XLSX

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                // Começa na linha 1 (segunda linha), pois a linha 0 é geralmente o cabeçalho
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        Cell celula5 = row.getCell(5); // Pega a célula do índice 5
                        if (celula5 != null && celula5.getCellType() == CellType.STRING) {
                            String term = celula5.getStringCellValue();
                            String category = alterandoDados(term);
                            System.out.println(sdf.format(System.currentTimeMillis()) + " | " + term + " => " + category);
                        }
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

    private static String classification;
    static List<String> listaModificados = new ArrayList<>();

    static String alterandoDados(String term) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyB8ockrzlb0PdYnkkm-AfKqSRrgQ6B0bRg"; // Update with your actual API key
        String jsonInputString = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"Classify the term: '%s' into one of the following categories if the term is 'Affected Code' do not make any change:\\n1. Software and Applications\\n2. Malware and Vulnerabilities\\n3. Frameworks and Libraries\\n4. Hardware and Firmware\\n5. Protocols and APIs\\n6. Development Tools and Packages\\nRespond with the category name only.\"}]}]}",
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

                    // Parse the classification from the response
                    classification = extrairApenasMudanca(response.toString());
                    listaModificados.add(classification);
                    return classification;

                } else if (responseCode == 429) { // Handling rate limiting
//                    System.out.println("Error: " + responseCode + " - Too Many Requests. Retrying...");
                    Thread.sleep((long) Math.pow(2, attempts) * 1000); // Exponential backoff
                    attempts++;
                } else {
//                    System.out.println("Error: " + responseCode + " - " + conn.getResponseMessage());
                    break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        return "Classification failed"; // In case of an error
    }

    // Method to parse the JSON response and extract the classification
    private static String extrairApenasMudanca(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");

        // Extract the "text" from the first candidate's content parts
        JSONObject candidate = candidatesArray.getJSONObject(0);
        JSONObject content = candidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        String classification = parts.getJSONObject(0).getString("text").trim();
        return classification; // <- variavel trocada ja pela IA
    }

    public static void exibirMudancas() {
        for (String mudanca : listaModificados) {
            System.out.println(mudanca);
        }
    }

}


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

public class TratamentoImpacto {
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
                        Cell celula49 = row.getCell(49);
                        if (celula49 != null && celula49.getCellType() == CellType.STRING) {
                            String term = celula49.getStringCellValue();
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
    static List<String> listaModificadosImpact = new ArrayList<>();

    static String alterandoDados(String term) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyB8ockrzlb0PdYnkkm-AfKqSRrgQ6B0bRg"; // Update with your actual API key
        String jsonInputString = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"Classify the term '%s' into one of the following categories: Data Extraction, Remote Code Execution, Backdoor Access, Data Damage, Payment Diversion, Others. Answer me only whith the name of classification.\"}]}]}",
                term
        );
        int attempts = 0;
        while (attempts < 10) {
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

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                    }

                    classification = extrairApenasMudanca(response.toString());
                    listaModificadosImpact.add(classification);
                    return classification;

                } else if (responseCode == 429) {
//                    System.out.println("Error: " + responseCode + " - Too Many Requests. Retrying...");
                    Thread.sleep((long) Math.pow(2, attempts) * 1000); // Aumento de tempo exponencial
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

    private static String extrairApenasMudanca(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);

        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");

        // Verifique se existem candidatos
        if (candidatesArray.length() > 0) {
            JSONObject candidate = candidatesArray.getJSONObject(0);

            // Verificar se o "finishReason" é "STOP", indicando uma resposta completa
            if (candidate.getString("finishReason").equals("STOP")) {
                // Verificar se o campo "content" existe
                if (candidate.has("content")) {
                    JSONObject content = candidate.getJSONObject("content");
                    JSONArray parts = content.getJSONArray("parts");

                    // Verifique se "parts" contém elementos
                    if (parts.length() > 0) {
                        String classification = parts.getJSONObject(0).getString("text").trim();
                        return classification; // Retorne a classificação
                    } else {
//                        System.out.println("Nenhuma parte encontrada no conteúdo.");
                    }
                } else {
//                    System.out.println("Campo 'content' não encontrado no JSON.");
                }
            } else {
                // Se o "finishReason" não for "STOP", trate de acordo
//                System.out.println("Resposta bloqueada por motivos de segurança: " + candidate.getString("finishReason"));
            }
        } else {
//            System.out.println("Nenhum candidato encontrado no JSON.");
        }

        return "Classification not found"; // Retorno em caso de falha
    }

    public static void exibirMudancasImpact() {
        for (String mudanca : listaModificadosImpact) {
            System.out.println(mudanca);
        }
    }

}



import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LeituraBucket {
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro:generateContent";
    private static final String GEMINI_API_KEY = "AIzaSyCSasvsyA5oYwu1PTq1fVee3NtCDRZwgPo"; // Substitua pela sua chave de API

    public static String askGemini(List<String> questions) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Construindo o JSON com segurança
        StringBuilder jsonBody = new StringBuilder("{\"contents\":[");
        for (String question : questions) {
            String escapedQuestion = question.replace("\"", "\\\""); // Escapando aspas
            jsonBody.append("{\"parts\":[{\"text\":\"").append(escapedQuestion).append("\"}]}").append(",");
        }

        // Remove a última vírgula e fecha o JSON
        if (questions.size() > 0) {
            jsonBody.setLength(jsonBody.length() - 1); // Remove última vírgula
        }
        jsonBody.append("]}");

        // Construindo a requisição
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEMINI_API_URL + "?key=" + GEMINI_API_KEY))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificando o status da resposta
        if (response.statusCode() != 200) {
            // Tratando erro 429 (Resource has been exhausted)
            if (response.statusCode() == 429) {
                throw new IOException("Gemini API request failed: Resource has been exhausted (check quota).");
            }
            throw new IOException("Gemini API request failed with status code: " + response.statusCode() + ". Response: " + response.body());
        }

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.body());

        // Verificando se há candidatos na resposta
        if (root.path("candidates").isMissingNode() || root.path("candidates").isEmpty()) {
            throw new IOException("The 'candidates' field is missing or empty in the Gemini AI response.");
        }

        JsonNode answerNode = root.path("candidates").get(0).path("content").path("parts").get(0).path("text");

        // Verificando se o texto da resposta está presente
        if (answerNode.isMissingNode() || answerNode.asText().isEmpty()) {
            throw new IOException("The 'response.text' field is missing or empty in the Gemini AI response.");
        }

        return answerNode.asText(); // Retorna o texto da resposta
    }

    public static void main(String[] args) {
        List<String> perguntas = new ArrayList<>();
        String bucketName = "s3-sprint";
        String key = "basededados.xlsx"; // Altere para a chave do seu arquivo no bucket
        Region region = Region.US_EAST_1; // Substitua pela região do seu bucket

        // Criação do cliente S3
        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (InputStream inputStream = s3.getObject(getObjectRequest);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                System.out.println("Folha: " + sheet.getSheetName());

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        // Verifica se a coluna está dentro das permitidas
                        if (cell.getColumnIndex() == 5) {
                            String valorOriginal = cell.getStringCellValue();
                            String pergunta = "Vou te passar 6 categorias e você me dirá em qual delas a palavra que eu te enviarei mais se aplica. " +
                                    "1- Software e Aplicações\n" +
                                    "2- Malware e Vulnerabilidades\n" +
                                    "3- Frameworks e Bibliotecas\n" +
                                    "4- Hardware e Firmware\n" +
                                    "5- Protocolos e APIs\n" +
                                    "6- Ferramentas de Desenvolvimento e Pacotes\n\n" +
                                    "Em categoria a palavra '" + valorOriginal + "' se encaixa melhor? Responda apenas com o nome da categoria.";

                            perguntas.add(pergunta); // Adiciona a pergunta à lista
                        }
                    }
                }

                // Envia todas as perguntas de uma só vez
                if (!perguntas.isEmpty()) {
                    List<String> respostas = Collections.singletonList(askGemini(perguntas));

                    // Armazena as respostas nas células correspondentes
                    int respostaIndex = 0;
                    for (int rowIndex = 0; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                        Row row = sheet.getRow(rowIndex);
                        for (Cell cell : row) {
                            if (cell.getColumnIndex() == 5 && respostaIndex < respostas.size()) {
                                cell.setCellValue(respostas.get(respostaIndex));
                                respostaIndex++;
                            }
                        }
                    }
                }
                System.out.println(); // Adiciona uma linha em branco entre as folhas
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (s3 != null) {
                s3.close();
            }
        }
    }
}
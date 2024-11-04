package org.techguard.tratativa;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LeituraETratativa {
    private static String classificationAffect;
    private static String classificationDownstreamTarget;
    private static String classificationImpact;
    public static List<String> listaDatas = new ArrayList<>();
    public static List<String> listaNomes = new ArrayList<>();
    public static List<String> listaAttackOuDisclosure = new ArrayList<>();
    public static List<String> listaModificadosAffect = new ArrayList<>();
    public static List<String> listaModificadosDownstreamTarget = new ArrayList<>();
    public static List<String> listaModificadosImpact = new ArrayList<>();

    public static void lerBucket() throws IOException {
        String bucketName = "pedro1297";
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

                s3.close();
            }
        }
    }

    public static void buscarDatasIncidentes() {
        String bucketName = "pedro1297";
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

        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");

        try (InputStream inputStream = s3.getObject(getObjectRequest);
             Workbook workbook = new XSSFWorkbook(inputStream)) { // Usando Apache POI para ler o arquivo XLSX

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                for (int rowIndex = 1; rowIndex <= 5; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        Cell celula0 = row.getCell(0);

                        if (celula0 != null && celula0.getCellType() == CellType.NUMERIC) {
                            Date term = celula0.getDateCellValue();
                            String termFormatado = sdf.format(term);
                            System.out.println(termFormatado);
                            listaDatas.add(termFormatado);
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

    public static void buscarNomesIncidentes() {
        String bucketName = "pedro1297";
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

                for (int rowIndex = 1; rowIndex <= 5; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        Cell celula1 = row.getCell(1);

                        if (celula1 != null && celula1.getCellType() == CellType.STRING) {
                            String term = celula1.getStringCellValue();
                            System.out.println(term);
                            listaNomes.add(term);
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

    public static void buscarAttackOuDisclosure() {
        String bucketName = "pedro1297";
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

                for (int rowIndex = 1; rowIndex <= 5; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        Cell celula2 = row.getCell(2);

                        if (celula2 != null && celula2.getCellType() == CellType.STRING) {
                            String term = celula2.getStringCellValue();
                            System.out.println(term);
                            listaAttackOuDisclosure.add(term);
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



    public static void tratandoDadosAffect() {
        String bucketName = "pedro1297";
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

                for (int rowIndex = 1; rowIndex <= 5; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        Cell celula5 = row.getCell(5);
                        if (celula5 != null && celula5.getCellType() == CellType.STRING) {
                            String term = celula5.getStringCellValue();
                            String category = alterandoDadosAffect(term);
                            System.out.println(rowIndex + " - " + sdf.format(System.currentTimeMillis()) + " | " + term + " => " + category);
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

    public static void tratandoDadosDownstreamTarget() {
        String bucketName = "pedro1297";
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

                for (int rowIndex = 1; rowIndex <= 5; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        Cell celula7 = row.getCell(7);
                        if (celula7 != null && celula7.getCellType() == CellType.STRING) {
                            String term = celula7.getStringCellValue();
                            String category = alterandoDadosDownstreamTarget(term);
                            System.out.println(rowIndex + " - " + sdf.format(System.currentTimeMillis()) + " | " + term + " => " + category);
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

    public static void tratandoDadosImpact() {
        String bucketName = "pedro1297";
        String key = "basededados.xlsx";
        Region region = Region.US_EAST_1;

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
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                for (int rowIndex = 1; rowIndex <= 5; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row != null) {
                        Cell celula49 = row.getCell(49);
                        if (celula49 != null && celula49.getCellType() == CellType.STRING) {
                            String term = celula49.getStringCellValue();
                            String category = alterandoDadosImpact(term);
                            System.out.println(rowIndex + " - " + sdf.format(System.currentTimeMillis()) + " | " + term + " => " + category);
                        }
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (s3 != null) {
                s3.close();
            }
        }
    }

    public static String alterandoDadosAffect(String term) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyDlti8js0JJDsZDviQ9bbUOzN6P2YXzUtA"; // Update with your actual API key
        String jsonInputString = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"Classify the term: '%s' into one of the following categories:\n1. Software and Applications\n2. Malware and Vulnerabilities\n3. Frameworks and Libraries\n4. Hardware and Firmware\n5. Protocols and APIs\n6. Development Tools and Packages\nRespond with the category name only(without numbers or special characters).\"}]}]}",
                term
        );

        Integer attempts = 0;
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

                    // Parse the classificationAffect from the response
                    classificationAffect = extrairApenasMudancaAffect(response.toString());
                    listaModificadosAffect.add(classificationAffect);
                    return classificationAffect; // <- inserir banco

                } else if (responseCode == 429) {
                    Thread.sleep((long) Math.pow(2, attempts) * 1000);
                    attempts++;
                } else {
                    break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        return "Classification falhou"; // In case of an error
    }

    public static String alterandoDadosDownstreamTarget(String term) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyDlti8js0JJDsZDviQ9bbUOzN6P2YXzUtA"; // Update with your actual API key
        String jsonInputString = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"Classify the term: '%s' into one of the following categories:\n1. Systems and Platform Users\n2. Software Applications and Libraries\n3. Companies and organizations\n4. Cryptocurrency and Finance Users\n5. Governments, Activists and Non-Governmental Organizations (NGOs)\n6. Developers and IT Professionals\nRespond with the category name only(without numbers or special characters).\"}]}]}",
                term
        );

        Integer attempts = 0;
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

                    // Parse the classificationAffect from the response
                    classificationDownstreamTarget = extrairApenasMudancaDownstreamTarget(response.toString());
                    listaModificadosDownstreamTarget.add(classificationDownstreamTarget);
                    return classificationDownstreamTarget; // <- inserir banco

                } else if (responseCode == 429) {
                    Thread.sleep((long) Math.pow(2, attempts) * 1000);
                    attempts++;
                } else {
                    break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        return "Classification falhou"; // In case of an error
    }

    public static String alterandoDadosImpact(String term) {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyDlti8js0JJDsZDviQ9bbUOzN6P2YXzUtA"; // Update with your actual API key
        String jsonInputString = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"Classify the term: '%s' into one of the following categories:\n1. Data Extraction\n2. Remote Code Execution\n3. Backdoor Access\n4. Data Damage\n5. Payment Diversion\n6. Others\nRespond with the category name only(without numbers or special characters).\"}]}]}",
                term
        );

        int tentativasImpact = 0;
        while (tentativasImpact < 10) {
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

                    classificationImpact = extrairApenasMudancaImpact(response.toString());
                    listaModificadosImpact.add(classificationImpact);
                    return classificationImpact; // <- inserir banco

                } else if (responseCode == 429) {
                    Thread.sleep((long) Math.pow(2, tentativasImpact) * 1000); // Aumento de tempo exponencial
                    tentativasImpact++;
                } else {
                    break;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        return "Classification falhou";
    }

    private static String extrairApenasMudancaAffect(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
        JSONObject candidate = candidatesArray.getJSONObject(0);
        JSONObject content = candidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        return parts.getJSONObject(0).getString("text").trim();
    }

    private static String extrairApenasMudancaDownstreamTarget(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
        JSONObject candidate = candidatesArray.getJSONObject(0);
        JSONObject content = candidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        return parts.getJSONObject(0).getString("text").trim();
    }

    private static String extrairApenasMudancaImpact(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
        if (candidatesArray.length() > 0) {
            JSONObject candidate = candidatesArray.getJSONObject(0);
            if (candidate.getString("finishReason").equals("STOP")) {
                if (candidate.has("content")) {
                    JSONObject content = candidate.getJSONObject("content");
                    JSONArray parts = content.getJSONArray("parts");
                    if (parts.length() > 0) {
                        String classification = parts.getJSONObject(0).getString("text").trim();
                        return classification;
                    }
                }
            }
        }
        return "Others";
    }


    public static void mostrandoTabela() {
        String bucketName = "pedro1297";
        String key = "basededados.xlsx";
        Region region = Region.US_EAST_1;

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
                    for (Cell cell : row) { // Data, Nome, Ataque/Vazamento, Afetado, Alvo Subsequente(Secundário), Impacto
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
                                if (cell.getColumnIndex() == 5) {
                                    if (cell.getRowIndex() == 0) {
                                        System.out.print("Affected Code \t");
                                    } else {
                                        String term = cell.getStringCellValue();
                                        String category = alterandoDadosAffect(term);
                                        System.out.print(listaModificadosAffect.get(cell.getRowIndex()) + "\t");
                                    }
                                } else if (cell.getColumnIndex() == 49) {
                                    if (cell.getRowIndex() == 0) {
                                        System.out.print("Impact \t");
                                    } else {
                                        String term2 = cell.getStringCellValue();
                                        String category = alterandoDadosImpact(term2);
                                        System.out.print(listaModificadosImpact.get(cell.getRowIndex()) + "\t");
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
                    System.out.println();
                }
                System.out.println();
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
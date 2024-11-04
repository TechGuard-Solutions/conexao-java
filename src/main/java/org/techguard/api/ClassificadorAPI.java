package org.techguard.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClassificadorAPI {
    private String apiKey;

    public ClassificadorAPI(String apiKey) {
        this.apiKey = apiKey;
    }
    public String classificar(String termo, String categoria) throws IOException, InterruptedException {
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + apiKey;
        String prompt = gerarPrompt(termo, categoria);
        String jsonInputString = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}", prompt);

        int tentativas = 0;
        while (tentativas < 10) {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

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

                    return extrairClassificacao(response.toString());

                } else if (responseCode == 429) { // Too Many Requests
                    Thread.sleep((long) Math.pow(2, tentativas) * 1000); // Backoff exponencial
                    tentativas++;
                } else {
                    throw new IOException("Erro na chamada da API: " + responseCode);
                }
            } catch (IOException e) {
                if (tentativas < 9) { // Retenta apenas 9x
                    tentativas++;
                    Thread.sleep(1000 * (long) Math.pow(2, tentativas)); // Backoff exponencial
                    continue;
                }
                throw new IOException("Excedeu o número máximo de tentativas. Erro original: " + e.getMessage(), e);
            }
        }
        return "Classification falhou"; // ou lance uma exceção
    }

    private String gerarPrompt(String termo, String categoria) {
        // Lógica para gerar o prompt correto com base na categoria
        switch (categoria) {
            case "Affect":
                return String.format("Classify the term: '%s' into one of the following categories:\n1. Software and Applications\n2. Malware and Vulnerabilities\n3. Frameworks and Libraries\n4. Hardware and Firmware\n5. Protocols and APIs\n6. Development Tools and Packages\nRespond with the category name only(without numbers or special characters).", termo);
            case "Downstream Target":
                return String.format("Classify the term: '%s' into one of the following categories:\n1. Systems and Platform Users\n2. Software Applications and Libraries\n3. companies and organizations\n4. Cryptocurrency and Finance Users\n5. Governments, Activists and Non-Governmental Organizations (NGOs)\n6. Developers and IT Professionals\nRespond with the category name only(without numbers or special characters).", termo);
            case "Impact":
                return String.format("Classify the term: '%s' into one of the following categories:\n1. Data Extraction\n2. Remote Code Execution\n3. Backdoor Access\n4. Data Damage\n5. Payment Diversion\n6. Others\nRespond with the category name only(without numbers or special characters).", termo);
            default:
                return "";
        }
    }

    private String extrairClassificacao(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray candidatesArray = jsonObject.getJSONArray("candidates");
        if (candidatesArray.isEmpty()) {
            return "Classificação não encontrada"; // Ou lance uma exceção
        }
        JSONObject candidate = candidatesArray.getJSONObject(0);
        JSONObject content = candidate.getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        if (parts.isEmpty()) {
            return "Classificação não encontrada"; // Ou lance uma exceção
        }


        return parts.getJSONObject(0).getString("text").trim();

    }
}
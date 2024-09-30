import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;


public class LeituraBucket {
//CREDÊNCIAS E INICIALIZAÇÃO DA CONSTRUÇÃO DE UM CLIENTE S3
    public class S3ReadFile {
        public static void main(String[] args) {
            String bucketName = "s3-raw-lab11";
            String key = "C:\\Users\\camar\\OneDrive\\Área de Trabalho\\SPTECH\\2 SEMESTRE\\GRUPO DE P.I\\PROJETO - PI\\AWS\\.aws";
            Region regions = Region.US_EAST_1; // Substitua pela sua região

            S3Client s3 = S3Client.builder()
                    .region(regions)
                    .credentialsProvider(ProfileCredentialsProvider.create())
                    .build();

// construção de um novo objeto
// Esse objeto é usado para solicitar um arquivo específico de um bucket no Amazon S3.

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("s3-raw-lab11")
                    .key("ler-base-dados")
                    .build();


            try {
                s3.getObject(getObjectRequest, Paths.get("arquivo-baixado.txt"));
                String content = new String(Files.readAllBytes(Paths.get("arquivo-baixado.txt")));
                System.out.println("Conteúdo do arquivo: " + content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}}

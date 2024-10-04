import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.Files;

public class LeituraBucket {
//CREDÊNCIAS E INICIALIZAÇÃO DA CONSTRUÇÃO DE UM CLIENTE S3
public static class S3ReadFile {
    public static void main(String[] args) {
        String bucketName = "s3-raw-lab11";
        String key = "arquivoBucket/conexao-java/basededados.xlsx";
        Region region = Region.US_EAST_1; // região do bucket

        S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

// construção de um novo objeto
// solicitar um arquivo específico de um bucket no Amazon S3.


        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();


            //aqui ele irá fazer a tentativa de verificar se o arquivo fez a leitura e foi baixado localmente

        try {
            s3.getObject(getObjectRequest, Paths.get("arquivo-baixado.xlsx"));
            String content = new String(Files.readAllBytes(Paths.get("arquivo-baixado.xlsx")));
            System.out.println("Conteúdo do arquivo: " + content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro ao baixar o arquivo do S3: " + e.getMessage());
        }
        }
}}

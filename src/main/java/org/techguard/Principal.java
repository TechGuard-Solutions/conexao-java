package org.techguard;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.techguard.dao.UsuarioDao;
import org.techguard.tabelas.Usuario;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class LeituraBucket {
    String bucketName = "s3";
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
        try (
    InputStream inputStream = s3.getObject(getObjectRequest);
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

    } catch (
    IOException e) {
        e.printStackTrace();
    } finally {
        // Feche o cliente S3
        s3.close();
    }
}


public class Principal {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        // Criação do objeto da classe usuario
        Usuario user = new Usuario();
        // Criação do objeto da classe usuarioDAO (Data Access Object)
        UsuarioDao usuarioDao = new UsuarioDao();
        // Setando os insert para o banco
        int opcao;
        do {
            System.out.println("Escolha uma opção:");
            System.out.println("1. Cadastrar novo usuário");
            System.out.println("2. Ver usuários cadastrados");
            System.out.println("3. Ver logs");
            System.out.println("0. Sair");
            opcao = leitor.nextInt();
            leitor.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o ID do usuário: ");
                    user.setIdUsuario(leitor.nextInt());
                    leitor.nextLine();
                    System.out.print("Digite o nome: ");
                    user.setNomeUsuario(leitor.nextLine());
                    System.out.print("Digite o CPF: ");
                    user.setCpf(leitor.nextLine());
                    System.out.print("Digite o telefone: ");
                    user.setTelUsuario(leitor.nextLine());
                    System.out.print("Digite o email: ");
                    user.setEmailUsuario(leitor.nextLine());
                    usuarioDao.cadastrarUsuario(user);
                    break;
                case 2:
                    usuarioDao.selectUsuario();
                    break;
                case 3:
                    System.out.println("Qual log deseja ver?");
                    System.out.println("1. Log MySQL");
                    System.out.println("2. Log Node");
                    System.out.println("3. Log Java");
                    System.out.println("4. Log Sistema EC2");
                    int opcaoLog = leitor.nextInt();
                    switch (opcaoLog) {
                        case 1:
                            System.out.println("log mysql...");
                            break;
                        case 2:
                            System.out.println("log node...");
                            break;
                        case 3:
                            System.out.println("log java...");
                            break;
                        case 4:
                            System.out.println("log ec2...");
                            break;
                        default:
                            System.out.println("""
                                    Este não é um log válido... 
                                    Digite a opçãp novamente: """);
                            leitor.nextInt();
                    }
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);

        leitor.close();
    }

}

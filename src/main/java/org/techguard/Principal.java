package org.techguard;

import org.techguard.dao.UsuarioDao;
import org.techguard.tabelas.Usuario;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;


public class Principal {
    public static void main(String[] args) throws IOException {

        for (int i = 1; i <= 3; i++) {
            if (i == 1){
                System.out.println("impact");
                LeituraBucket.tratandoDadosImpact();
            } else if (i == 2){
                System.out.println("affect");
                LeituraBucket.tratandoDadosAffect();
            } else if (i == 3) {
                LeituraBucket.mostrandoTabela();
            }
        }

        LeituraBucket fazerLeituraBucket = new LeituraBucket();
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
            System.out.println("4. Ler base de dados do S3");
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
                    user.setFkEmpresa(1);
                    user.setFkTipoUsuario(1);
                    usuarioDao.cadastrarUsuario(user);
                    System.out.println("Usuário cadastrado!");
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
                                    Digite a opção novamente: """);
                            leitor.nextInt();
                    }
                case 4:
                    fazerLeituraBucket.lerBucket();
                    break;
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


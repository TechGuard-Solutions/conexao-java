package org.techguard;

import org.techguard.dao.LeituraETratativaDAO;
import org.techguard.dao.UsuarioDao;
import org.techguard.tabelas.Usuario;
import org.techguard.tratativa.LeituraETratativa;

import java.io.IOException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException {

        LeituraETratativaDAO leituraETratativaDAO = new LeituraETratativaDAO();

        for (int i = 1; i <= 7; i++) {
            if (i == 1) {
                System.out.println("Data");
                LeituraETratativa.buscarDatasIncidentes();
            } else if (i == 2) {
                System.out.println("Nomes");
                LeituraETratativa.buscarNomesIncidentes();
            } else if (i == 3) {
                System.out.println("Attack/Disclosure");
                LeituraETratativa.buscarAttackOuDisclosure();
            } else if (i == 4) {
                System.out.println("Downstream Target");
                LeituraETratativa.tratandoDadosDownstreamTarget();
            } else if (i == 5) {
                System.out.println("affect");
                LeituraETratativa.tratandoDadosAffect();

            } else if (i == 6) {
                System.out.println("impact");
                LeituraETratativa.tratandoDadosImpact();
            } else if (i == 7) {
//                LeituraETratativa.mostrandoTabela();
            }
        }

        LeituraETratativa fazerLeituraETratativa = new LeituraETratativa();
        leituraETratativaDAO.cadastrarDados(fazerLeituraETratativa);
    }
}
package org.techguard.tabelas;

public class Usuario {
    private int idUsuario;
    private String nomeUsuario;
    private String senhaUsuario;
    private String cpf;
    private String emailUsuario;
    private String telUsuario;
    private int fkEmpresa;
    private int fkTipoUsuario;

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getTelUsuario() {
        return telUsuario;
    }

    public void setTelUsuario(String telUsuario) {
        this.telUsuario = telUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenhaUsuario() {
        return senhaUsuario;
    }

    public void setSenhaUsuario(String senhaUsuario) {
        this.senhaUsuario = senhaUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public int getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(int fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public int getFkTipoUsuario() {
        return fkTipoUsuario;
    }

    public void setFkTipoUsuario(int fkTipoUsuario) {
        this.fkTipoUsuario = fkTipoUsuario;
    }
}

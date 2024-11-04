package org.techguard.modelo;

import java.util.Date;

public class Incidente {
    private Date data;
    private String nome;
    private String tipoIncidente;
    private String affected;
    private String downstreamTarget;
    private String impact;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoIncidente() {
        return tipoIncidente;
    }

    public void setTipoIncidente(String tipoIncidente) {
        this.tipoIncidente = tipoIncidente;
    }

    public String getAffected() {
        return affected;
    }

    public void setAffected(String affected) {
        this.affected = affected;
    }

    public String getDownstreamTarget() {
        return downstreamTarget;
    }

    public void setDownstreamTarget(String downstreamTarget) {
        this.downstreamTarget = downstreamTarget;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    @Override
    public String toString() {
        return "Incidente" +
                "data=" + data +
                ", nome='" + nome + '\'' +
                ", tipoIncidente='" + tipoIncidente + '\'' +
                ", affected='" + affected + '\'' +
                ", downstreamTarget='" + downstreamTarget + '\'' +
                ", impact='" + impact + '\'' +
                '}';
    }
}
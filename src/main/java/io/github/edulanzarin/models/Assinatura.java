package io.github.edulanzarin.models;

import java.time.LocalDate;

public class Assinatura {

    private String id;
    private String usuarioId;
    private String pagamentoId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativa;
    private String tipoPlano; // "MENSAL", "TRIMESTRAL", "SEMESTRAL", "VITALICIO"

    // Construtor
    public Assinatura(String id, String usuarioId, String pagamentoId,
            LocalDate dataInicio, String tipoPlano, boolean ativa) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.pagamentoId = pagamentoId;
        this.dataInicio = dataInicio;
        this.tipoPlano = tipoPlano;
        this.ativa = ativa;
        this.dataFim = calcularDataFim(dataInicio, tipoPlano);
    }

    // Getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(String pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    // Método para calcular a data de término baseado no plano
    private LocalDate calcularDataFim(LocalDate inicio, String tipoPlano) {
        if ("VITALICIO".equals(tipoPlano)) {
            return inicio.plusYears(100); // 100 anos = vitalício
        } else if ("SEMESTRAL".equals(tipoPlano)) {
            return inicio.plusMonths(6);
        } else if ("TRIMESTRAL".equals(tipoPlano)) {
            return inicio.plusMonths(3);
        } else { // MENSAL
            return inicio.plusMonths(1);
        }
    }

    @Override
    public String toString() {
        return "Assinatura [id=" + id + ", usuarioId=" + usuarioId + ", pagamentoId=" + pagamentoId + ", dataInicio="
                + dataInicio + ", dataFim=" + dataFim + ", ativa=" + ativa + ", tipoPlano=" + tipoPlano + "]";
    }
}
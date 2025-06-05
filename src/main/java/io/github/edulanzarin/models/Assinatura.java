package io.github.edulanzarin.models;

import java.time.LocalDate;

public class Assinatura {
    private String id;
    private String usuarioId;
    private String pagamentoId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativa;

    // Construtor atualizado para remover o plano
    public Assinatura(String id, String usuarioId, String pagamentoId,
            LocalDate dataInicio, LocalDate dataFim, boolean ativa) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.pagamentoId = pagamentoId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativa = ativa;
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

    @Override
    public String toString() {
        return "Assinatura [id=" + id + ", usuarioId=" + usuarioId + ", pagamentoId=" + pagamentoId
                + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim
                + ", ativa=" + ativa + "]";
    }
}
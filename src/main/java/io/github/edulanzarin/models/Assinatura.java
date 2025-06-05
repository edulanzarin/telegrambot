package io.github.edulanzarin.models;

import java.time.LocalDate;

/*
 * Classe responsável por criar o modelo de assinatura.
 * Contém referência ao usuário e ao pagamento que a originou,
 * além dos dados do plano, datas de início e fim.
 */
public class Assinatura {

    private String id;
    private String usuarioId; // Referência ao usuário
    private String pagamentoId; // Pagamento que originou esta assinatura
    private PlanoAssinatura plano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private boolean ativa;

    // Construtor padrão
    public Assinatura(String id, String usuarioId, String pagamentoId,
            PlanoAssinatura plano, LocalDate dataInicio, LocalDate dataFim) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.pagamentoId = pagamentoId;
        this.plano = plano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.ativa = true; // Por padrão, a assinatura é criada como ativa
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

    public PlanoAssinatura getPlano() {
        return plano;
    }

    public void setPlano(PlanoAssinatura plano) {
        this.plano = plano;
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
                + ", plano=" + plano + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim
                + ", ativa=" + ativa + "]";
    }
}
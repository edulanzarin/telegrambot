package io.github.edulanzarin.models;

import java.time.LocalDate;

/**
 * Representa uma assinatura de um usuário, contendo informações sobre o
 * plano contratado, o período de validade, o status de ativação e a
 * referência ao pagamento relacionado.
 * 
 * Tipos de plano disponíveis:
 * - "MENSAL"
 * - "TRIMESTRAL"
 * - "SEMESTRAL"
 * - "VITALICIO"
 * 
 * A data de término da assinatura é calculada automaticamente com base no tipo
 * de plano.
 */
public class Assinatura {

    private String id; // ID da assinatura
    private String usuarioId; // ID do usuário associado
    private String pagamentoId; // ID do pagamento correspondente
    private LocalDate dataInicio; // Data de início da assinatura
    private LocalDate dataFim; // Data de término da assinatura (calculada)
    private boolean ativa; // Status da assinatura
    private String tipoPlano; // Tipo de plano contratado

    /**
     * Cria uma nova instância de {@code Assinatura}, com a data de término
     * definida automaticamente de acordo com o tipo de plano.
     * 
     * @param id          Identificador único da assinatura
     * @param usuarioId   ID do usuário associado
     * @param pagamentoId ID do pagamento vinculado
     * @param dataInicio  Data de início da assinatura
     * @param tipoPlano   Tipo do plano ("MENSAL", "TRIMESTRAL", etc.)
     * @param ativa       Status de ativação (true para ativa)
     */
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

    // Getters e setters padrão
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

    /**
     * Calcula a data de término da assinatura com base no tipo de plano contratado.
     *
     * @param inicio    Data de início da assinatura
     * @param tipoPlano Tipo do plano ("MENSAL", "TRIMESTRAL", etc.)
     * @return Data de término calculada
     */
    private LocalDate calcularDataFim(LocalDate inicio, String tipoPlano) {
        switch (tipoPlano) {
            case "VITALICIO":
                return inicio.plusYears(100); // Simula plano vitalício
            case "SEMESTRAL":
                return inicio.plusMonths(6);
            case "TRIMESTRAL":
                return inicio.plusMonths(3);
            default:
                return inicio.plusMonths(1); // Plano mensal como padrão
        }
    }

    @Override
    public String toString() {
        return "Assinatura [id=" + id + ", usuarioId=" + usuarioId + ", pagamentoId=" + pagamentoId
                + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim + ", ativa=" + ativa
                + ", tipoPlano=" + tipoPlano + "]";
    }
}

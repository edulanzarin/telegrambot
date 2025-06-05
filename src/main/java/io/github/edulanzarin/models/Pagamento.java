package io.github.edulanzarin.models;

import java.time.LocalDateTime;

/*
 * Classe responsável pelo modelo do Pagamento.
 * O id do pagamento será retirado do próprio id gerado pelo Mercado Pago,
 * contém referência ao usuário e ao plano solicitado, status do pagamento
 * e data de expiração (3 horas após criação).
 */
public class Pagamento {

    private String id;
    private String usuarioId; // Referência ao usuário
    private PlanoAssinatura plano; // Plano solicitado
    private LocalDateTime vencimento;
    private String status; // "PENDENTE", "APROVADO", "RECUSADO", "CANCELADO"

    // Construtor com status padrão como PENDENTE
    public Pagamento(String id, String usuarioId, PlanoAssinatura plano) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.plano = plano;
        this.vencimento = LocalDateTime.now().plusHours(3);
        this.status = "PENDENTE";
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

    public PlanoAssinatura getPlano() {
        return plano;
    }

    public void setPlano(PlanoAssinatura plano) {
        this.plano = plano;
    }

    public LocalDateTime getVencimento() {
        return vencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Pagamento [id=" + id + ", usuarioId=" + usuarioId + ", plano=" + plano
                + ", vencimento=" + vencimento + ", status=" + status + "]";
    }
}
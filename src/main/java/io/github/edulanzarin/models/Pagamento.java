package io.github.edulanzarin.models;

import java.time.LocalDateTime;

/**
 * Representa uma solicitação de pagamento de um plano de assinatura.
 * 
 * Esta classe é responsável por armazenar as informações relacionadas a um
 * pagamento,
 * incluindo o ID gerado pela plataforma (ex: Mercado Pago), o usuário que
 * solicitou,
 * o tipo de plano adquirido, o valor e o status da transação.
 * 
 * O campo {@code vencimento} define a data limite para o pagamento, configurada
 * automaticamente para três horas após a criação do objeto.
 * 
 * Possíveis valores para {@code status} incluem:
 * - "PENDENTE": Aguardando pagamento
 * - "APROVADO": Pagamento confirmado
 * - "RECUSADO": Pagamento rejeitado
 * - "CANCELADO": Pagamento cancelado manualmente ou expirado
 * 
 * O plano adquirido pode ser: "MENSAL", "TRIMESTRAL", "SEMESTRAL" ou
 * "VITALICIO".
 */
public class Pagamento {

    private String id; // ID do pagamento (gerado pelo Mercado Pago)
    private String usuarioId; // Referência ao ID do usuário que efetuou o pagamento
    private LocalDateTime vencimento; // Data e hora de expiração do pagamento
    private String status; // Status atual do pagamento
    private String plano; // Tipo de plano adquirido
    private double valor; // Valor monetário do pagamento

    /**
     * Cria uma nova instância de {@code Pagamento} com status inicial como
     * "PENDENTE"
     * e vencimento automático para 3 horas a partir da criação.
     *
     * @param id        ID da transação (provido pela plataforma de pagamento)
     * @param usuarioId ID do usuário associado ao pagamento
     * @param plano     Tipo de plano solicitado
     * @param valor     Valor do plano em reais
     */
    public Pagamento(String id, String usuarioId, String plano, double valor) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.vencimento = LocalDateTime.now().plusHours(3);
        this.status = "PENDENTE";
        this.plano = plano;
        this.valor = valor;
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

    public LocalDateTime getVencimento() {
        return vencimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlano() {
        return plano;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "Pagamento [id=" + id + ", usuarioId=" + usuarioId + ", vencimento=" + vencimento
                + ", status=" + status + ", plano=" + plano + ", valor=" + valor + "]";
    }
}

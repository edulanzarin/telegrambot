package io.github.edulanzarin.models;

import java.time.LocalDateTime;

/*
 * Classe responsável pelo modelo do Pagamento.
 * O id do pagamento será retirado do próprio id gerado pelo Mercado Pago,
 * a data de expiração do pagamento será sempre de 3 horas e o restante
 * das informações serão retiradas do objeto Assinatura.
 */
public class Pagamento {

    private String id;
    private Assinatura assinatura;
    private final LocalDateTime vencimento;

    /* Construtor com duração do pagamento sempre de 3 horas */
    public Pagamento(String id, Assinatura assinatura) {
        this.id = id;
        this.assinatura = assinatura;
        this.vencimento = LocalDateTime.now().plusHours(3);
    }

    /* Getters e setters */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAssinatura(Assinatura assinatura) {
        this.assinatura = assinatura;
    }

    public Assinatura getAssinatura() {
        return assinatura;
    }

    public LocalDateTime getVencimento() {
        return vencimento;
    }

    @Override
    public String toString() {
        return "Pagamento [id=" + id + ", assinatura=" + assinatura + " vencimento=" + vencimento + ", ]";
    }
}
package io.github.edulanzarin.models;

import java.math.BigDecimal;

/*
 * Planos de Assinaturas disponíveis e suas respectivas durações e valores.
 * Uso de BigDecimal para maior controle monetário.
 * Essa classe será usada em "Assinatura" para representar  o plano de assinatura.
 */
public enum PlanoAssinatura {

    UM_MES(1, new BigDecimal("14.90")),
    TRES_MESES(3, new BigDecimal("29.90")),
    SEIS_MESES(6, new BigDecimal("44.90")),
    VITALICIO(-1, new BigDecimal("59.90"));

    private final int duracaoMeses;
    private final BigDecimal preco;

    PlanoAssinatura(int duracaoMeses, BigDecimal preco) {
        this.duracaoMeses = duracaoMeses;
        this.preco = preco;
    }

    /* Apenas getters, não tem setters pois os valores são fixos */
    public int getDuracaoMeses() {
        return duracaoMeses;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}

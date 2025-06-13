package io.github.edulanzarin.models;

/**
 * Representa os tipos de plano de assinatura disponíveis, com suas
 * características.
 */
public enum TipoPlano {
    MENSAL(1, 14.90, "Plano Mensal"),
    TRIMESTRAL(3, 24.90, "Plano Trimestral"),
    SEMESTRAL(6, 39.90, "Plano Semestral"),
    VITALICIO(1200, 59.90, "Plano Vitalício");

    private final int mesesDuracao;
    private final double valor;
    private final String descricao;

    TipoPlano(int mesesDuracao, double valor, String descricao) {
        this.mesesDuracao = mesesDuracao;
        this.valor = valor;
        this.descricao = descricao;
    }

    public int getMesesDuracao() {
        return mesesDuracao;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }
}
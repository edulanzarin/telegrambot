package io.github.edulanzarin.models;

/**
 * Representa os tipos de plano de assinatura disponíveis, com suas
 * características.
 */
public enum TipoPlano {
    MENSAL(1, 29.90, "Plano Mensal"),
    TRIMESTRAL(3, 79.90, "Plano Trimestral (10% de desconto)"),
    SEMESTRAL(6, 149.90, "Plano Semestral (15% de desconto)"),
    VITALICIO(1200, 999.90, "Plano Vitalício"); // 1200 meses = 100 anos

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
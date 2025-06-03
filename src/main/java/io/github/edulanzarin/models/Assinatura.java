package io.github.edulanzarin.models;

import java.time.LocalDate;

/*
 * Classe responsável por criar o modelo de assinatura.
 * Usando "TipoAssinatura" para representação do plano e assinatura.
 * id da assinatura (aleatório), plano (há 4 planos), 
 * data de início da assinatura e data do fim e usuario ao qual pertence a assinatura. 
 */
public class Assinatura {

    private String id;
    private PlanoAssinatura plano;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Usuario usuario;

    /* Construtor padrão */
    public Assinatura(String id, PlanoAssinatura plano, LocalDate dataInicio, LocalDate dataFim, Usuario usuario) {
        this.id = id;
        this.plano = plano;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.usuario = usuario;
    }

    /* Getters e setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*
     * Assinatura terá um dos 4 planos disponíveis.
     */
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Assinatura [id=" + id + ", plano=" + plano + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim
                + ", usuario=" + usuario + "]";
    }
}
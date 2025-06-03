package io.github.edulanzarin.models;

import java.time.LocalDateTime;

/*
 * Classe responsável pelo modelo dos Eventnso.
 * Armazena o tipo de evento (/start, /help, callback_xyz, mensagem)
 * E cada evento é atrelado a um Usuário.
 */
public class Evento {

    private String id;
    private String tipoEvento;
    private LocalDateTime dataHora;

    /* Getters e setters */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Evento [id=" + id + ", tipoEvento=" + tipoEvento + ", dataHora=" + dataHora + "]";
    }
}

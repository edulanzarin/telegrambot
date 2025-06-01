package io.github.edulanzarin.models;

import java.time.LocalDateTime;

/*
 * Classe responsável pelo modelo das mensagens recebidas,
 * cada mensagem pertence a um usuário e será armazenada no banco durante
 * 30 dias, para não sobrecarregar o armazenamento.
 */
public class Mensagem {

    private String id;
    private String conteudo;
    private LocalDateTime dataHora;
    private Usuario usuario;

    /*
     * Construtor padrão
     */
    public Mensagem(String id, String conteudo, LocalDateTime dataHora, Usuario usuario) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataHora = dataHora;
        this.usuario = usuario;
    }

    /*
     * Getters e setters
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Mensagem [id=" + id + ", conteudo=" + conteudo + ", dataHora=" + dataHora + ", usuario=" + usuario
                + "]";
    }
}

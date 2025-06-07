package io.github.edulanzarin.models;

import java.time.LocalDateTime;

/**
 * Representa uma mensagem recebida de um usuário.
 * 
 * A classe {@code Mensagem} é responsável por armazenar o conteúdo textual
 * enviado por um usuário, juntamente com o instante em que a mensagem foi
 * registrada no sistema.
 * 
 * Cada mensagem é associada a um {@link Usuario} e permanece armazenada por até
 * 30 dias para fins de histórico, auditoria ou análise, evitando sobrecarga de
 * armazenamento no banco de dados.
 */
public class Mensagem {

    private String id; // Identificador único da mensagem
    private String conteudo; // Conteúdo textual da mensagem
    private LocalDateTime dataEnvio; // Data e hora de envio da mensagem
    private Usuario usuario; // Usuário que enviou a mensagem

    /**
     * Cria uma nova instância de {@code Mensagem}.
     * 
     * @param id        Identificador único da mensagem
     * @param conteudo  Texto enviado pelo usuário
     * @param dataEnvio Data e hora do envio
     * @param usuario   Usuário associado à mensagem
     */
    public Mensagem(String id, String conteudo, LocalDateTime dataEnvio, Usuario usuario) {
        this.id = id;
        this.conteudo = conteudo;
        this.dataEnvio = dataEnvio;
        this.usuario = usuario;
    }

    // Getters e setters padrão

    /**
     * Retorna o ID da mensagem.
     * 
     * @return ID único da mensagem
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID da mensagem.
     * 
     * @param id Identificador único
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna o conteúdo da mensagem.
     * 
     * @return Texto da mensagem
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * Define o conteúdo da mensagem.
     * 
     * @param conteudo Texto enviado pelo usuário
     */
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    /**
     * Retorna a data e hora de envio da mensagem.
     * 
     * @return Data e hora do envio
     */
    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    /**
     * Define a data e hora de envio da mensagem.
     * 
     * @param dataEnvio Instante em que a mensagem foi enviada
     */
    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    /**
     * Retorna o usuário que enviou a mensagem.
     * 
     * @return Objeto {@code Usuario} associado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Define o usuário associado à mensagem.
     * 
     * @param usuario Usuário remetente
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Mensagem [id=" + id + ", conteudo=" + conteudo + ", dataEnvio=" + dataEnvio + ", usuario=" + usuario
                + "]";
    }
}

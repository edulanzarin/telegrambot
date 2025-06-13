package io.github.edulanzarin.models;

import java.time.LocalDateTime;

/**
 * Representa uma interação registrada de um usuário com o sistema.
 * 
 * A classe {@code Evento} é utilizada para armazenar informações sobre eventos
 * disparados durante o uso do bot ou da aplicação, como comandos enviados,
 * mensagens, callbacks de botões ou outras ações relevantes.
 * 
 * Cada evento pode ser vinculado posteriormente a um usuário, permitindo
 * rastreabilidade e análise de comportamento.
 * 
 * Exemplos de valores possíveis para {@code tipoEvento} incluem:
 * - "/start": Inicialização do bot
 * - "/help": Solicitação de ajuda
 * - "mensagem": Mensagem de texto genérica
 * - "callback_xyz": Interação com botão do tipo callback
 */
public class Evento {

    private String id; // ID único do evento (UUID)
    private String tipoEvento; // Tipo de evento (ex: /start, mensagem, callback_xyz)
    private LocalDateTime dataHora; // Data e hora em que o evento foi registrado
    private String usuarioId; // ID do usuário que disparou o evento

    // Getters e setters padrão

    /**
     * Retorna o ID único do evento.
     * 
     * @return Identificador do evento
     */
    public String getId() {
        return id;
    }

    /**
     * Define o ID do evento.
     * 
     * @param id Identificador único (UUID)
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retorna o tipo ou descrição do evento.
     * 
     * @return Tipo de evento
     */
    public String getTipoEvento() {
        return tipoEvento;
    }

    /**
     * Define o tipo ou descrição do evento.
     * 
     * @param tipoEvento Comando ou interação registrada
     */
    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    /**
     * Retorna a data e hora em que o evento foi registrado.
     * 
     * @return Data e hora do evento
     */
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    /**
     * Define a data e hora do evento.
     * 
     * @param dataHora Momento da ocorrência do evento
     */
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * Retorna o ID do usuário associado ao evento.
     * 
     * @return ID do usuário
     */
    public String getUsuarioId() {
        return usuarioId;
    }

    /**
     * Define o ID do usuário associado ao evento.
     * 
     * @param usuarioId ID do usuário
     */
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return "Evento [id=" + id + ", tipoEvento=" + tipoEvento +
                ", dataHora=" + dataHora + ", usuarioId=" + usuarioId + "]";
    }
}

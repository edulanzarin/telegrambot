package io.github.edulanzarin.handlers;

import org.telegram.telegrambots.meta.api.objects.Message;
import io.github.edulanzarin.core.Bot;

public class ComandosHandler {
    private final MensagensHandler mensagensHandler;
    private final RespostasHandler respostasHandler;

    public ComandosHandler(Bot bot) {
        this.respostasHandler = new RespostasHandler(bot);
        this.mensagensHandler = new MensagensHandler();
    }

    public void processarMensagemRecebida(Message mensagem) {
        if (mensagem == null) {
            throw new IllegalArgumentException("Mensagem não pode ser nula");
        }
        mensagensHandler.processar(mensagem, respostasHandler);
    }
}
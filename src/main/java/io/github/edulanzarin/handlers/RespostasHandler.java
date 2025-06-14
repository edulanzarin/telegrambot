package io.github.edulanzarin.handlers;

import io.github.edulanzarin.core.Bot;
import io.github.edulanzarin.utils.Respostas;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class RespostasHandler {
    private final Bot bot;
    private final Respostas respostas;

    public RespostasHandler(Bot bot) {
        this.bot = bot;
        this.respostas = Respostas.getInstance();
    }

    public void enviarResposta(long chatId, String texto) {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(String.valueOf(chatId));
        mensagem.setText(texto);
        mensagem.setParseMode("HTML");

        try {
            bot.execute(mensagem);
        } catch (TelegramApiException e) {
            System.err.println("[ERRO] Ao enviar mensagem: " + e.getMessage());
        }
    }

    public void enviarRespostaGenerica(long chatId) {
        enviarResposta(chatId, respostas.mensagemPadrao());
    }
}
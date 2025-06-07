package io.github.edulanzarin.handlers;

import io.github.edulanzarin.core.Bot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MensagensHandler {
    public void processarMensagem(Message message, Bot bot) {
        String texto = message.getText();
        long chatId = message.getChatId();

        if (texto.startsWith("/")) {
            processarComando(texto, chatId, bot);
        } else {
            responderMensagemPadrao(chatId, bot);
        }
    }

    private void processarComando(String comando, long chatId, Bot bot) {
        String resposta = switch (comando) {
            case "/start" -> "Olá! Bem-vindo ao bot!";
            case "/help" -> "Aqui estão os comandos disponíveis:\n/start\n/help";
            default -> "Comando não reconhecido.";
        };

        enviarMensagem(chatId, resposta, bot);
    }

    private void responderMensagemPadrao(long chatId, Bot bot) {
        enviarMensagem(chatId, "Você disse algo que não é um comando. Use /help para ver as opções.", bot);
    }

    private void enviarMensagem(long chatId, String texto, Bot bot) {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(String.valueOf(chatId));
        mensagem.setText(texto);

        try {
            bot.execute(mensagem);
        } catch (TelegramApiException e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
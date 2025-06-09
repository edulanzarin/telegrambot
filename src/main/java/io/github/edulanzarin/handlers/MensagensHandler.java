package io.github.edulanzarin.handlers;

import io.github.edulanzarin.core.Bot;
import io.github.edulanzarin.models.Usuario;
import io.github.edulanzarin.utils.Respostas;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MensagensHandler {
    private final Respostas respostas;

    public MensagensHandler() {
        this.respostas = Respostas.getInstance();
    }

    public void processarMensagem(Message message, Bot bot) {
        String texto = message.getText();
        long chatId = message.getChatId();
        Usuario usuario = criarUsuarioAPartirDaMensagem(message);

        if (texto.startsWith("/")) {
            processarComando(texto, chatId, usuario, bot);
        } else {
            responderMensagemPadrao(chatId, bot);
        }
    }

    private void processarComando(String comando, long chatId, Usuario usuario, Bot bot) {
        String resposta;

        switch (comando) {
            case "/start":
                resposta = respostas.comandoStart(usuario);
                // Aqui você pode adicionar o envio do vídeo e outras mensagens iniciais
                break;
            case "/help":
                resposta = respostas.comandoHelp();
                break;
            default:
                resposta = respostas.comandoNaoReconhecido();
                break;
        }

        enviarMensagem(chatId, resposta, bot);
    }

    private void responderMensagemPadrao(long chatId, Bot bot) {
        enviarMensagem(chatId, respostas.mensagemPadrao(), bot);
    }

    private Usuario criarUsuarioAPartirDaMensagem(Message message) {
        return new Usuario(
                String.valueOf(message.getFrom().getId()),
                message.getFrom().getUserName(),
                message.getFrom().getFirstName());
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
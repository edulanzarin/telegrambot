package io.github.edulanzarin.handlers;

import io.github.edulanzarin.models.Evento;
import io.github.edulanzarin.models.Usuario;
import io.github.edulanzarin.services.FirebaseService;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.UUID;

public class MensagensHandler {
    private final ProcessamentoComandosHandler comandosHandler;

    public MensagensHandler() {
        this.comandosHandler = new ProcessamentoComandosHandler();
    }

    public void processar(Message mensagem, RespostasHandler respostasHandler) {
        String texto = mensagem.getText();
        long chatId = mensagem.getChatId();
        Usuario usuario = criarUsuario(mensagem);

        registrarDados(usuario, texto);

        if (texto != null && texto.startsWith("/")) {
            String resposta = comandosHandler.processarComando(texto, usuario);
            respostasHandler.enviarResposta(chatId, resposta);
        } else {
            respostasHandler.enviarRespostaGenerica(chatId);
        }
    }

    private Usuario criarUsuario(Message mensagem) {
        return new Usuario(
                String.valueOf(mensagem.getFrom().getId()),
                mensagem.getFrom().getUserName(),
                mensagem.getFrom().getFirstName());
    }

    private void registrarDados(Usuario usuario, String tipoEvento) {
        try {
            FirebaseService.verificarECadastrarUsuario(usuario);

            Evento evento = new Evento();
            evento.setId(UUID.randomUUID().toString());
            evento.setUsuarioId(usuario.getId());
            evento.setDataHora(LocalDateTime.now());
            evento.setTipoEvento(tipoEvento != null && tipoEvento.startsWith("/") ? tipoEvento : "mensagem");

            FirebaseService.registrarEvento(evento);
        } catch (Exception e) {
            System.err.println("[ERRO] Falha no registro: " + e.getMessage());
        }
    }
}
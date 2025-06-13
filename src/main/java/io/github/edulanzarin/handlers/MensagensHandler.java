package io.github.edulanzarin.handlers;

import io.github.edulanzarin.core.Bot;
import io.github.edulanzarin.models.Usuario;
import io.github.edulanzarin.services.FirebaseService;
import io.github.edulanzarin.utils.Respostas;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Classe handler responsável por processar e responder às mensagens recebidas
 * pelo bot do Telegram.
 * Implementa a lógica de roteamento de comandos e gerenciamento de respostas.
 */
public class MensagensHandler {
    // Instância singleton do gerenciador de respostas padrão
    private final Respostas respostas;

    /**
     * Constrói uma nova instância do handler de mensagens.
     * Inicializa o gerenciador de respostas padrão.
     */
    public MensagensHandler() {
        this.respostas = Respostas.getInstance();
    }

    /**
     * Processa a mensagem recebida e direciona para o método adequado.
     * 
     * @param message Objeto Message recebido da API do Telegram
     * @param bot     Instância do bot para envio de respostas
     * @throws IllegalArgumentException Se message for nulo
     */
    public void processarMensagemRecebida(Message message, Bot bot) {
        if (message == null) {
            throw new IllegalArgumentException("Message não pode ser nulo");
        }

        String texto = message.getText();
        long chatId = message.getChatId();
        Usuario usuario = construirUsuario(message);

        if (texto != null && texto.startsWith("/")) {
            processarComando(texto, chatId, usuario, bot);
        } else {
            responderGenerico(chatId, bot);
        }
    }

    /**
     * Processa um comando específico recebido do usuário.
     * 
     * @param comando Comando a ser processado (começa com "/")
     * @param chatId  ID do chat para envio da resposta
     * @param usuario Objeto contendo informações do usuário
     * @param bot     Instância do bot para envio da resposta
     */
    private void processarComando(String comando, long chatId, Usuario usuario, Bot bot) {
        String resposta;

        // Verifica e cadastra o usuário no Firebase
        try {
            FirebaseService.verificarECadastrarUsuario(usuario);
        } catch (Exception e) {
            System.err.println("Erro ao verificar/cadastrar usuário no Firebase: " + e.getMessage());
            e.printStackTrace(); // TODO: Substituir por logger profissional
        }

        switch (comando) {
            case "/start":
                resposta = respostas.comandoStart(usuario);
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

    /**
     * Envia uma resposta genérica para mensagens não comandos.
     * 
     * @param chatId ID do chat para envio da resposta
     * @param bot    Instância do bot para envio da mensagem
     */
    private void responderGenerico(long chatId, Bot bot) {
        enviarMensagem(chatId, respostas.mensagemPadrao(), bot);
    }

    /**
     * Constrói um objeto Usuario a partir das informações da mensagem recebida.
     * 
     * @param message Objeto Message recebido do Telegram
     * @return Objeto Usuario populado com os dados do remetente
     */
    private Usuario construirUsuario(Message message) {
        return new Usuario(
                String.valueOf(message.getFrom().getId()),
                message.getFrom().getUserName(),
                message.getFrom().getFirstName());
    }

    /**
     * Envia efetivamente uma mensagem para o chat especificado.
     * 
     * @param chatId ID do chat de destino
     * @param texto  Conteúdo da mensagem a ser enviada
     * @param bot    Instância do bot para execução do envio
     * @throws TelegramApiException Se ocorrer erro na comunicação com a API do
     *                              Telegram
     */
    private void enviarMensagem(long chatId, String texto, Bot bot) {
        SendMessage mensagem = new SendMessage();
        mensagem.setChatId(String.valueOf(chatId));
        mensagem.setText(texto);
        mensagem.setParseMode("HTML"); // <-- Ativa interpretação HTML no Telegram

        try {
            bot.execute(mensagem);
        } catch (TelegramApiException e) {
            // TODO: Substituir por logger profissional
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
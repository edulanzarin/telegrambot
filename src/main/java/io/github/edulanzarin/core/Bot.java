package io.github.edulanzarin.core;

import io.github.edulanzarin.handlers.MensagensHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {
    private final String botUsername;
    private final MensagensHandler mensagensHandler;

    public Bot(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
        this.mensagensHandler = new MensagensHandler();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            mensagensHandler.processarMensagem(update.getMessage(), this);
        }
    }
}
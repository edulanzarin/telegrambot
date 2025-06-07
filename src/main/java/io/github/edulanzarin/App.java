package io.github.edulanzarin;

import io.github.edulanzarin.core.Bot;
import io.github.edulanzarin.utils.CarregarEnv;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    public static void main(String[] args) {
        // Carrega variáveis do arquivo .env
        CarregarEnv.load();

        try {
            String botToken = System.getProperty("TELEGRAM_BOT_TOKEN");
            String botUsername = System.getProperty("TELEGRAM_BOT_USERNAME");

            if (botToken == null || botUsername == null) {
                throw new IllegalStateException(
                        "Variáveis TELEGRAM_BOT_TOKEN e TELEGRAM_BOT_USERNAME devem ser configuradas no arquivo .env");
            }

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot(botToken, botUsername));
            System.out.println("Bot iniciado com sucesso!");
        } catch (TelegramApiException e) {
            System.err.println("Erro ao iniciar o bot: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalStateException e) {
            System.err.println("Erro de configuração: " + e.getMessage());
            System.exit(1);
        }
    }
}
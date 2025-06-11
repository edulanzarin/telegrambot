package io.github.edulanzarin.config;

import java.time.Duration;

/**
 * Classe de configuração centralizada para o Telegram Bot.
 * Contém todas as constantes, configurações e valores padrão utilizados na aplicação.
 *
 * Esta classe segue o padrão de configuração centralizada para facilitar
 * manutenção e modificações futuras.
 */
public final class Config {

    // Construtor privado para evitar instanciação
    private Config() {
        throw new UnsupportedOperationException("Esta é uma classe utilitária e não pode ser instanciada");
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DO FIREBASE
     * -----------------------------------------------------------
     */

    public static final class Firebase {
        // Nomes das coleções no Firestore
        public static final String COLLECTION_USUARIOS = "usuarios";
        public static final String COLLECTION_PAGAMENTOS = "pagamentos";
        public static final String COLLECTION_ASSINATURAS = "assinaturas";
        public static final String COLLECTION_RESPOSTAS = "respostas";
        public static final String COLLECTION_EVENTOS = "eventos";
        public static final String COLLECTION_MENSAGENS = "mensagens";

        // Campos obrigatórios das variáveis de ambiente
        public static final String[] REQUIRED_ENV_VARS = {
            "FIREBASE_TYPE",
            "FIREBASE_PROJECT_ID",
            "FIREBASE_PRIVATE_KEY_ID",
            "FIREBASE_PRIVATE_KEY",
            "FIREBASE_CLIENT_EMAIL",
            "FIREBASE_CLIENT_ID",
            "FIREBASE_AUTH_URI",
            "FIREBASE_TOKEN_URI",
            "FIREBASE_AUTH_PROVIDER_CERT_URL",
            "FIREBASE_CLIENT_CERT_URL",
            "FIREBASE_UNIVERSE_DOMAIN"
        };

        // Timeouts para operações
        public static final Duration READ_TIMEOUT = Duration.ofSeconds(10);
        public static final Duration WRITE_TIMEOUT = Duration.ofSeconds(15);
        public static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(5);
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DO TELEGRAM BOT
     * -----------------------------------------------------------
     */

    public static final class TelegramBot {
        // Variáveis de ambiente obrigatórias
        public static final String ENV_BOT_TOKEN = "TELEGRAM_BOT_TOKEN";
        public static final String ENV_BOT_USERNAME = "TELEGRAM_BOT_USERNAME";

        // Limites de mensagens
        public static final int MAX_MESSAGE_LENGTH = 4096;
        public static final int MAX_CAPTION_LENGTH = 1024;

        // Timeouts
        public static final Duration MESSAGE_TIMEOUT = Duration.ofSeconds(30);
        public static final Duration CALLBACK_TIMEOUT = Duration.ofSeconds(10);

        // Rate limiting
        public static final int MAX_MESSAGES_PER_SECOND = 30;
        public static final int MAX_MESSAGES_PER_MINUTE = 20;
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DE PAGAMENTO
     * -----------------------------------------------------------
     */

    public static final class Pagamento {
        // Tempo de expiração padrão para pagamentos
        public static final Duration TEMPO_EXPIRACAO_PADRAO = Duration.ofHours(3);

        // Valores mínimos e máximos
        public static final double VALOR_MINIMO = 1.00;
        public static final double VALOR_MAXIMO = 9999.99;

        // Tentativas de retry para confirmação
        public static final int MAX_TENTATIVAS_CONFIRMACAO = 3;
        public static final Duration INTERVALO_RETRY = Duration.ofSeconds(5);
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DE ASSINATURA
     * -----------------------------------------------------------
     */

    public static final class Assinatura {
        // Período de graça antes da expiração
        public static final Duration PERIODO_GRACA = Duration.ofDays(3);

        // Intervalo para verificação de assinaturas expiradas
        public static final Duration INTERVALO_VERIFICACAO_EXPIRACAO = Duration.ofHours(6);

        // Dias de antecedência para notificar sobre expiração
        public static final int DIAS_AVISO_EXPIRACAO = 7;
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DE MENSAGENS E RESPOSTAS
     * -----------------------------------------------------------
     */

    public static final class Mensagens {
        // Chaves das mensagens padrão
        public static final String CHAVE_BEM_VINDO = "bem_vindo";
        public static final String CHAVE_HELP = "help";
        public static final String CHAVE_COMANDO_NAO_RECONHECIDO = "comando_nao_reconhecido";
        public static final String CHAVE_MENSAGEM_PADRAO = "mensagem_padrao";
        public static final String CHAVE_VIDEO_INICIAL = "video_inicial";
        public static final String CHAVE_INFORMACOES_GRUPO = "informacoes_grupo";
        public static final String CHAVE_BOTOES_PLANOS = "botoes_planos";

        // Mensagens de erro padrão
        public static final String ERRO_GENERICO = "Desculpe, ocorreu um erro. Tente novamente mais tarde.";
        public static final String ERRO_COMANDO_INVALIDO = "Comando não reconhecido. Digite /help para ver os comandos disponíveis.";
        public static final String ERRO_ACESSO_NEGADO = "Você não tem permissão para executar este comando.";

        // Mensagens de sucesso
        public static final String SUCESSO_CADASTRO = "Usuário cadastrado com sucesso!";
        public static final String SUCESSO_PAGAMENTO = "Pagamento processado com sucesso!";
        public static final String SUCESSO_ASSINATURA = "Assinatura ativada com sucesso!";

        // Timeout para cache de mensagens
        public static final Duration CACHE_TIMEOUT = Duration.ofMinutes(30);
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DE LOGGING
     * -----------------------------------------------------------
     */

    public static final class Logging {
        // Níveis de log por contexto
        public static final String LEVEL_ROOT = "INFO";
        public static final String LEVEL_FIREBASE = "INFO";
        public static final String LEVEL_TELEGRAM = "WARN";
        public static final String LEVEL_HANDLERS = "DEBUG";

        // Configurações de arquivos de log
        public static final String LOG_DIR = "logs";
        public static final String LOG_FILE_PATTERN = "telegrambot.%d{yyyy-MM-dd}.%i.log";
        public static final String ERROR_LOG_PATTERN = "error.%d{yyyy-MM-dd}.%i.log";
        public static final String MAX_FILE_SIZE = "10MB";
        public static final int MAX_HISTORY_DAYS = 30;
        public static final String TOTAL_SIZE_CAP = "1GB";
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES GERAIS DA APLICAÇÃO
     * -----------------------------------------------------------
     */

    public static final class App {
        // Informações da aplicação
        public static final String NOME = "Telegram Bot";
        public static final String VERSAO = "1.0.0";
        public static final String AUTOR = "Eduardo Lanzarin";

        // Configurações de thread pool
        public static final int CORE_POOL_SIZE = 5;
        public static final int MAX_POOL_SIZE = 20;
        public static final Duration KEEP_ALIVE_TIME = Duration.ofMinutes(5);

        // Configurações de retry
        public static final int MAX_RETRY_ATTEMPTS = 3;
        public static final Duration INITIAL_RETRY_DELAY = Duration.ofSeconds(1);
        public static final double RETRY_MULTIPLIER = 2.0;

        // Configurações de cache
        public static final long MAX_CACHE_SIZE = 1000;
        public static final Duration CACHE_EXPIRATION = Duration.ofMinutes(15);
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DE VALIDAÇÃO
     * -----------------------------------------------------------
     */

    public static final class Validation {
        // Regex patterns
        public static final String REGEX_USER_ID = "^[0-9]{1,20}$";
        public static final String REGEX_USERNAME = "^[a-zA-Z0-9_]{5,32}$";
        public static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // Limites de tamanho
        public static final int MAX_USERNAME_LENGTH = 32;
        public static final int MIN_USERNAME_LENGTH = 5;
        public static final int MAX_NAME_LENGTH = 100;
        public static final int MIN_NAME_LENGTH = 2;

        // Limites de dados
        public static final int MAX_PAYLOAD_SIZE = 1024 * 1024; // 1MB
        public static final int MAX_BATCH_SIZE = 100;
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DE SEGURANÇA
     * -----------------------------------------------------------
     */

    public static final class Security {
        // Rate limiting
        public static final int MAX_REQUESTS_PER_MINUTE = 60;
        public static final int MAX_REQUESTS_PER_HOUR = 1000;

        // Timeouts de segurança
        public static final Duration SESSION_TIMEOUT = Duration.ofMinutes(30);
        public static final Duration TOKEN_EXPIRATION = Duration.ofHours(24);

        // Configurações de hash
        public static final String HASH_ALGORITHM = "SHA-256";
        public static final int SALT_LENGTH = 32;

        // IPs e domínios permitidos (pode ser configurado via ambiente)
        public static final String[] ALLOWED_DOMAINS = {
            "api.telegram.org",
            "firestore.googleapis.com"
        };
    }

    /*
     * -----------------------------------------------------------
     * CONFIGURAÇÕES DE MONITORAMENTO
     * -----------------------------------------------------------
     */

    public static final class Monitoring {
        // Métricas
        public static final Duration METRICS_COLLECTION_INTERVAL = Duration.ofMinutes(1);
        public static final int MAX_METRICS_HISTORY = 1000;

        // Health checks
        public static final Duration HEALTH_CHECK_INTERVAL = Duration.ofMinutes(5);
        public static final Duration HEALTH_CHECK_TIMEOUT = Duration.ofSeconds(10);

        // Alertas
        public static final double ERROR_RATE_THRESHOLD = 0.05; // 5%
        public static final Duration RESPONSE_TIME_THRESHOLD = Duration.ofSeconds(5);
        public static final int MEMORY_USAGE_THRESHOLD_PERCENT = 85;
    }
}

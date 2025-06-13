package io.github.edulanzarin.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;
import io.github.edulanzarin.models.Assinatura;
import io.github.edulanzarin.models.Evento;
import io.github.edulanzarin.models.Pagamento;
import io.github.edulanzarin.models.TipoPlano;
import io.github.edulanzarin.models.Usuario;
import io.github.edulanzarin.utils.CarregarEnv;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Serviço de integração com o Firebase Firestore.
 * Gerencia a conexão com o banco de dados e fornece operações CRUD para:
 * - Usuários
 * - Pagamentos
 * - Assinaturas
 *
 * Todas as operações são thread-safe e incluem validações robustas.
 */
public class FirebaseService {

        private static final Logger logger = Logger.getLogger(
                        FirebaseService.class.getName());
        private static volatile Firestore db;
        private static final Object initLock = new Object();

        // Constantes para coleções
        private static final String COLLECTION_USUARIOS = "usuarios";
        private static final String COLLECTION_PAGAMENTOS = "pagamentos";
        private static final String COLLECTION_ASSINATURAS = "assinaturas";
        private static final String COLLECTION_RESPOSTAS = "respostas";
        private static final String COLLECTION_EVENTOS = "eventos";

        // Campos obrigatórios das variáveis de ambiente
        private static final String[] REQUIRED_ENV_VARS = {
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
                        "FIREBASE_UNIVERSE_DOMAIN",
        };

        /*
         * -----------------------------------------------------------
         * CONFIGURAÇÃO E INICIALIZAÇÃO
         * -----------------------------------------------------------
         */

        /**
         * Inicializa automaticamente a conexão com o Firebase quando a classe é
         * carregada
         */
        static {
                CarregarEnv.load();
                initialize();
        }

        /**
         * Configura e inicializa a conexão com o Firebase Firestore de forma
         * thread-safe.
         * Usa credenciais das variáveis de ambiente com validação robusta.
         *
         * @throws FirebaseInitializationException se a inicialização falhar
         */
        public static void initialize() {
                if (db != null)
                        return;

                synchronized (initLock) {
                        if (db != null)
                                return; // Double-check locking

                        try {
                                validateEnvironmentVariables();

                                JsonObject credentials = buildCredentialsJson();
                                FirebaseOptions options = FirebaseOptions.builder()
                                                .setCredentials(
                                                                GoogleCredentials.fromStream(
                                                                                new ByteArrayInputStream(
                                                                                                credentials
                                                                                                                .toString()
                                                                                                                .getBytes(StandardCharsets.UTF_8))))
                                                .build();

                                if (FirebaseApp.getApps().isEmpty()) {
                                        FirebaseApp.initializeApp(options);
                                        logger.log(
                                                        Level.INFO,
                                                        "✅ Firebase inicializado com sucesso");
                                }

                                db = FirestoreClient.getFirestore();
                        } catch (Exception e) {
                                logger.log(Level.SEVERE, "❌ Erro ao inicializar Firebase", e);
                                throw new FirebaseInitializationException(
                                                "Falha na inicialização do Firebase",
                                                e);
                        }
                }
        }

        /**
         * Valida se todas as variáveis de ambiente necessárias estão presentes
         */
        private static void validateEnvironmentVariables() {
                for (String envVar : REQUIRED_ENV_VARS) {
                        String value = System.getProperty(envVar); // agora usando System.getProperty
                        if (value == null || value.trim().isEmpty()) {
                                throw new IllegalStateException(
                                                "Variável de ambiente obrigatória não encontrada: " + envVar);
                        }
                }
        }

        /**
         * Constrói o objeto JSON com as credenciais do Firebase
         */
        private static JsonObject buildCredentialsJson() {
                JsonObject json = new JsonObject();
                json.addProperty("type", System.getProperty("FIREBASE_TYPE"));
                json.addProperty("project_id", System.getProperty("FIREBASE_PROJECT_ID"));
                json.addProperty("private_key_id", System.getProperty("FIREBASE_PRIVATE_KEY_ID"));
                json.addProperty("private_key", System.getProperty("FIREBASE_PRIVATE_KEY").replace("\\n", "\n"));
                json.addProperty("client_email", System.getProperty("FIREBASE_CLIENT_EMAIL"));
                json.addProperty("client_id", System.getProperty("FIREBASE_CLIENT_ID"));
                json.addProperty("auth_uri", System.getProperty("FIREBASE_AUTH_URI"));
                json.addProperty("token_uri", System.getProperty("FIREBASE_TOKEN_URI"));
                json.addProperty("auth_provider_x509_cert_url", System.getProperty("FIREBASE_AUTH_PROVIDER_CERT_URL"));
                json.addProperty("client_x509_cert_url", System.getProperty("FIREBASE_CLIENT_CERT_URL"));
                json.addProperty("universe_domain", System.getProperty("FIREBASE_UNIVERSE_DOMAIN"));
                return json;
        }

        /**
         * Verifica se o Firebase foi inicializado corretamente
         *
         * @throws IllegalStateException se o Firestore não estiver inicializado
         */
        private static void checkInitialization() {
                if (db == null) {
                        logger.log(
                                        Level.SEVERE,
                                        "Firebase não foi inicializado corretamente");
                        throw new IllegalStateException(
                                        "Firebase não foi inicializado corretamente");
                }
        }

        /*
         * -----------------------------------------------------------
         * OPERAÇÕES DE USUÁRIO
         * -----------------------------------------------------------
         */

        /**
         * Cadastra um novo usuário se ele não existir
         *
         * @param usuario Objeto Usuario com os dados do usuário
         * @return true se o usuário foi cadastrado, false se já existia
         * @throws IllegalArgumentException se o usuário for nulo ou inválido
         */
        public static boolean verificarECadastrarUsuario(Usuario usuario) {
                validateUser(usuario);
                checkInitialization();

                try {
                        DocumentReference docRef = db
                                        .collection(COLLECTION_USUARIOS)
                                        .document(usuario.getId());

                        if (docRef.get().get().exists()) {
                                logger.log(
                                                Level.INFO,
                                                "Usuário {0} já existe",
                                                usuario.getId());
                                return false;
                        }

                        Map<String, Object> data = createUserData(usuario);
                        docRef.set(data).get();

                        logger.log(
                                        Level.INFO,
                                        "Usuário {0} cadastrado com sucesso",
                                        usuario.getId());
                        return true;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(
                                        Level.SEVERE,
                                        "Erro ao cadastrar usuário " + usuario.getId(),
                                        e);
                        throw new FirebaseOperationException(
                                        "Erro ao cadastrar usuário",
                                        e);
                } catch (Exception e) {
                        logger.log(
                                        Level.SEVERE,
                                        "Erro inesperado ao cadastrar usuário " + usuario.getId(),
                                        e);
                        return false;
                }
        }

        /**
         * Cria o mapa de dados do usuário para inserção no Firestore
         */
        private static Map<String, Object> createUserData(Usuario usuario) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", usuario.getId());
                data.put("usuario", usuario.getUsuario());
                data.put("nome", usuario.getNome());
                data.put("assinaturaId", usuario.getAssinaturaId());
                return data;
        }

        /**
         * Valida se o objeto usuário é válido
         */
        private static void validateUser(Usuario usuario) {
                if (usuario == null) {
                        throw new IllegalArgumentException("Usuário não pode ser nulo");
                }
                if (usuario.getId() == null || usuario.getId().trim().isEmpty()) {
                        throw new IllegalArgumentException("ID do usuário é obrigatório");
                }
                if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                        throw new IllegalArgumentException("Nome do usuário é obrigatório");
                }
        }

        /**
         * Busca um usuário pelo ID
         *
         * @param usuarioId ID do usuário
         * @return Objeto Usuario ou null se não encontrado
         * @throws ExecutionException   se houver erro na execução
         * @throws InterruptedException se a operação for interrompida
         */
        public static Usuario buscarUsuario(String usuarioId)
                        throws ExecutionException, InterruptedException {
                validateUserId(usuarioId);
                checkInitialization();

                logger.log(Level.INFO, "Buscando usuário {0}", usuarioId);

                try {
                        DocumentSnapshot doc = db
                                        .collection(COLLECTION_USUARIOS)
                                        .document(usuarioId)
                                        .get()
                                        .get();

                        if (!doc.exists()) {
                                logger.log(Level.INFO, "Usuário {0} não encontrado", usuarioId);
                                return null;
                        }

                        Usuario usuario = createUserFromDocument(doc);
                        logger.log(
                                        Level.INFO,
                                        "Usuário {0} encontrado com sucesso",
                                        usuarioId);
                        return usuario;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(Level.SEVERE, "Erro ao buscar usuário " + usuarioId, e);
                        throw e;
                }
        }

        /**
         * Cria um objeto Usuario a partir de um DocumentSnapshot
         */
        private static Usuario createUserFromDocument(DocumentSnapshot doc) {
                Usuario usuario = new Usuario(
                                doc.getString("id"),
                                doc.getString("usuario"),
                                doc.getString("nome"));
                usuario.setAssinaturaId(doc.getString("assinaturaId"));
                return usuario;
        }

        /**
         * Valida o ID do usuário
         */
        private static void validateUserId(String usuarioId) {
                if (usuarioId == null || usuarioId.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "ID do usuário não pode ser nulo ou vazio");
                }
        }

        /*
         * -----------------------------------------------------------
         * OPERAÇÕES DE PAGAMENTO
         * -----------------------------------------------------------
         */

        /**
         * Cria um novo registro de pagamento
         *
         * @param pagamento Objeto Pagamento com os dados
         * @return ID do pagamento criado
         * @throws FirebaseOperationException se houver erro na operação
         */
        public static String criarPagamento(Pagamento pagamento) {
                validatePayment(pagamento);
                checkInitialization();

                logger.log(
                                Level.INFO,
                                "Criando pagamento para usuário {0}",
                                pagamento.getUsuarioId());

                try {
                        DocumentReference ref = db
                                        .collection(COLLECTION_PAGAMENTOS)
                                        .document(pagamento.getId());
                        Map<String, Object> data = createPaymentData(pagamento);

                        ref.set(data).get();
                        logger.log(
                                        Level.INFO,
                                        "Pagamento {0} criado com sucesso",
                                        pagamento.getId());
                        return ref.getId();
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(
                                        Level.SEVERE,
                                        "Erro ao criar pagamento " + pagamento.getId(),
                                        e);
                        throw new FirebaseOperationException("Erro ao criar pagamento", e);
                } catch (Exception e) {
                        logger.log(
                                        Level.SEVERE,
                                        "Erro inesperado ao criar pagamento " + pagamento.getId(),
                                        e);
                        throw new FirebaseOperationException("Erro ao criar pagamento", e);
                }
        }

        /**
         * Cria o mapa de dados do pagamento para inserção no Firestore
         */
        private static Map<String, Object> createPaymentData(Pagamento pagamento) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", pagamento.getId());
                data.put("usuarioId", pagamento.getUsuarioId());
                data.put(
                                "vencimento",
                                Timestamp.of(
                                                Date.from(
                                                                pagamento
                                                                                .getVencimento()
                                                                                .atZone(ZoneId.systemDefault())
                                                                                .toInstant())));
                data.put("status", pagamento.getStatus().name());
                data.put("plano", pagamento.getPlano().name());
                data.put("valor", pagamento.getValor());
                return data;
        }

        /**
         * Valida se o objeto pagamento é válido
         */
        private static void validatePayment(Pagamento pagamento) {
                if (pagamento == null) {
                        throw new IllegalArgumentException("Pagamento não pode ser nulo");
                }
                if (pagamento.getId() == null || pagamento.getId().trim().isEmpty()) {
                        throw new IllegalArgumentException("ID do pagamento é obrigatório");
                }
                if (pagamento.getUsuarioId() == null ||
                                pagamento.getUsuarioId().trim().isEmpty()) {
                        throw new IllegalArgumentException("ID do usuário é obrigatório");
                }
                if (pagamento.getPlano() == null) {
                        throw new IllegalArgumentException("Tipo de plano é obrigatório");
                }
        }

        /**
         * Confirma um pagamento e cria a assinatura correspondente
         *
         * @param pagamentoId ID do pagamento a ser confirmado
         * @return true se a operação foi bem-sucedida
         * @throws FirebaseOperationException se houver erro na operação
         */
        public static boolean confirmarPagamento(String pagamentoId) {
                validatePaymentId(pagamentoId);
                checkInitialization();

                logger.log(Level.INFO, "Confirmando pagamento {0}", pagamentoId);

                try {
                        // 1. Atualizar status do pagamento
                        db
                                        .collection(COLLECTION_PAGAMENTOS)
                                        .document(pagamentoId)
                                        .update("status", Pagamento.StatusPagamento.APROVADO.name())
                                        .get();

                        logger.log(
                                        Level.INFO,
                                        "Status do pagamento {0} atualizado para APROVADO",
                                        pagamentoId);

                        // 2. Buscar dados do pagamento
                        DocumentSnapshot pagamentoDoc = db
                                        .collection(COLLECTION_PAGAMENTOS)
                                        .document(pagamentoId)
                                        .get()
                                        .get();

                        if (!pagamentoDoc.exists()) {
                                logger.log(
                                                Level.WARNING,
                                                "Pagamento {0} não encontrado",
                                                pagamentoId);
                                throw new IllegalArgumentException("Pagamento não encontrado");
                        }

                        String usuarioId = pagamentoDoc.getString("usuarioId");
                        String tipoPlanoStr = pagamentoDoc.getString("plano");

                        if (usuarioId == null || tipoPlanoStr == null) {
                                logger.log(
                                                Level.WARNING,
                                                "Dados incompletos no pagamento {0}",
                                                pagamentoId);
                                throw new IllegalStateException(
                                                "Dados incompletos no pagamento");
                        }

                        // 3. Criar nova assinatura
                        TipoPlano tipoPlano = TipoPlano.valueOf(tipoPlanoStr);
                        String assinaturaId = criarAssinatura(
                                        usuarioId,
                                        pagamentoId,
                                        tipoPlano);

                        // 4. Atualizar usuário com ID da assinatura
                        db
                                        .collection(COLLECTION_USUARIOS)
                                        .document(usuarioId)
                                        .update("assinaturaId", assinaturaId)
                                        .get();

                        logger.log(
                                        Level.INFO,
                                        "Usuário {0} atualizado com a assinatura {1}",
                                        new Object[] { usuarioId, assinaturaId });

                        return true;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(
                                        Level.SEVERE,
                                        "Erro ao confirmar pagamento " + pagamentoId,
                                        e);
                        throw new FirebaseOperationException(
                                        "Erro ao confirmar pagamento",
                                        e);
                } catch (Exception e) {
                        logger.log(
                                        Level.SEVERE,
                                        "Erro inesperado ao confirmar pagamento " + pagamentoId,
                                        e);
                        return false;
                }
        }

        /**
         * Cria uma nova assinatura
         */
        private static String criarAssinatura(
                        String usuarioId,
                        String pagamentoId,
                        TipoPlano tipoPlano) throws ExecutionException, InterruptedException {
                DocumentReference assinaturaRef = db
                                .collection(COLLECTION_ASSINATURAS)
                                .document();
                LocalDate inicio = LocalDate.now();

                Assinatura assinatura = new Assinatura(
                                assinaturaRef.getId(),
                                usuarioId,
                                pagamentoId,
                                inicio,
                                tipoPlano,
                                true);

                Map<String, Object> assinaturaData = createSubscriptionData(assinatura);
                assinaturaRef.set(assinaturaData).get();

                logger.log(
                                Level.INFO,
                                "Assinatura {0} criada com sucesso para o usuário {1}",
                                new Object[] { assinaturaRef.getId(), usuarioId });

                return assinaturaRef.getId();
        }

        /**
         * Cria o mapa de dados da assinatura para inserção no Firestore
         */
        private static Map<String, Object> createSubscriptionData(
                        Assinatura assinatura) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", assinatura.getId());
                data.put("usuarioId", assinatura.getUsuarioId());
                data.put("pagamentoId", assinatura.getPagamentoId());
                data.put(
                                "dataInicio",
                                Timestamp.of(
                                                Date.from(
                                                                assinatura
                                                                                .getDataInicio()
                                                                                .atStartOfDay(ZoneId.systemDefault())
                                                                                .toInstant())));
                data.put("tipoPlano", assinatura.getTipoPlano().name());
                data.put("ativa", assinatura.isAtiva());
                data.put(
                                "dataFim",
                                Timestamp.of(
                                                Date.from(
                                                                assinatura
                                                                                .getDataFim()
                                                                                .atStartOfDay(ZoneId.systemDefault())
                                                                                .toInstant())));
                return data;
        }

        /**
         * Valida o ID do pagamento
         */
        private static void validatePaymentId(String pagamentoId) {
                if (pagamentoId == null || pagamentoId.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "ID do pagamento não pode ser nulo ou vazio");
                }
        }

        /*
         * -----------------------------------------------------------
         * OPERAÇÕES DE ASSINATURA
         * -----------------------------------------------------------
         */

        /**
         * Busca uma assinatura pelo ID
         *
         * @param assinaturaId ID da assinatura
         * @return Objeto Assinatura ou null se não encontrada
         * @throws ExecutionException   se houver erro na execução
         * @throws InterruptedException se a operação for interrompida
         */
        public static Assinatura buscarAssinatura(String assinaturaId)
                        throws ExecutionException, InterruptedException {
                validateSubscriptionId(assinaturaId);
                checkInitialization();

                logger.log(Level.INFO, "Buscando assinatura {0}", assinaturaId);

                try {
                        DocumentSnapshot doc = db
                                        .collection(COLLECTION_ASSINATURAS)
                                        .document(assinaturaId)
                                        .get()
                                        .get();

                        if (!doc.exists() || !hasRequiredSubscriptionFields(doc)) {
                                logger.log(
                                                Level.WARNING,
                                                "Assinatura {0} não encontrada ou dados incompletos",
                                                assinaturaId);
                                return null;
                        }

                        Assinatura assinatura = createSubscriptionFromDocument(doc);
                        logger.log(
                                        Level.INFO,
                                        "Assinatura {0} encontrada com sucesso",
                                        assinaturaId);
                        return assinatura;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(
                                        Level.SEVERE,
                                        "Erro ao buscar assinatura " + assinaturaId,
                                        e);
                        throw e;
                }
        }

        /**
         * Verifica se o documento contém os campos obrigatórios da assinatura
         */
        private static boolean hasRequiredSubscriptionFields(DocumentSnapshot doc) {
                return (doc.contains("dataInicio") &&
                                doc.contains("usuarioId") &&
                                doc.contains("pagamentoId") &&
                                doc.contains("ativa") &&
                                doc.contains("tipoPlano"));
        }

        /**
         * Cria um objeto Assinatura a partir de um DocumentSnapshot
         */
        private static Assinatura createSubscriptionFromDocument(
                        DocumentSnapshot doc) {
                // Tratamento seguro para dataInicio
                Timestamp inicioTimestamp = doc.get("dataInicio", Timestamp.class);
                if (inicioTimestamp == null) {
                        throw new IllegalStateException(
                                        "Data de início nula para assinatura: " + doc.getId());
                }

                LocalDate inicio = inicioTimestamp
                                .toDate()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                // Tratamento para tipoPlano
                String tipoPlanoStr = doc.getString("tipoPlano");
                if (tipoPlanoStr == null) {
                        throw new IllegalStateException(
                                        "Tipo de plano nulo para assinatura: " + doc.getId());
                }

                // Tratamento para o campo ativa
                Boolean ativa = doc.getBoolean("ativa");
                if (ativa == null) {
                        logger.log(
                                        Level.WARNING,
                                        "Campo 'ativa' nulo para assinatura: " + doc.getId());
                        ativa = false;
                }

                return new Assinatura(
                                doc.getString("id"),
                                doc.getString("usuarioId"),
                                doc.getString("pagamentoId"),
                                inicio,
                                TipoPlano.valueOf(tipoPlanoStr),
                                ativa);
        }

        /**
         * Valida o ID da assinatura
         */
        private static void validateSubscriptionId(String assinaturaId) {
                if (assinaturaId == null || assinaturaId.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "ID da assinatura não pode ser nulo ou vazio");
                }
        }

        /**
         * Verifica se um usuário possui assinatura ativa
         *
         * @param usuarioId ID do usuário
         * @return true se o usuário tem assinatura válida
         * @throws ExecutionException   se houver erro na execução
         * @throws InterruptedException se a operação for interrompida
         */
        public static boolean verificarAssinaturaAtiva(String usuarioId)
                        throws ExecutionException, InterruptedException {
                validateUserId(usuarioId);
                checkInitialization();

                logger.log(
                                Level.INFO,
                                "Verificando assinatura ativa para usuário {0}",
                                usuarioId);

                try {
                        Usuario usuario = buscarUsuario(usuarioId);
                        if (usuario == null || usuario.getAssinaturaId() == null) {
                                logger.log(
                                                Level.INFO,
                                                "Usuário {0} não possui assinatura ou não existe",
                                                usuarioId);
                                return false;
                        }

                        Assinatura assinatura = buscarAssinatura(usuario.getAssinaturaId());
                        if (assinatura == null) {
                                logger.log(
                                                Level.INFO,
                                                "Assinatura do usuário {0} não encontrada",
                                                usuarioId);
                                return false;
                        }

                        boolean ativa = isSubscriptionActive(assinatura);
                        logger.log(
                                        Level.INFO,
                                        "Status da assinatura para usuário {0}: {1}",
                                        new Object[] { usuarioId, ativa ? "ATIVA" : "INATIVA" });

                        return ativa;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(
                                        Level.SEVERE,
                                        "Erro ao verificar assinatura ativa para usuário " + usuarioId,
                                        e);
                        throw e;
                }
        }

        /**
         * Verifica se uma assinatura está ativa
         */
        private static boolean isSubscriptionActive(Assinatura assinatura) {
                LocalDate hoje = LocalDate.now();
                return (assinatura.isAtiva() &&
                                (assinatura.getDataFim() == null ||
                                                hoje.isBefore(assinatura.getDataFim()) ||
                                                hoje.isEqual(assinatura.getDataFim())));
        }

        /**
         * Cancela uma assinatura (marca como inativa)
         *
         * @param assinaturaId ID da assinatura
         * @return true se a operação foi bem-sucedida
         * @throws FirebaseOperationException se houver erro na operação
         */
        public static boolean cancelarAssinatura(String assinaturaId) {
                validateSubscriptionId(assinaturaId);
                checkInitialization();

                logger.log(Level.INFO, "Cancelando assinatura {0}", assinaturaId);

                try {
                        db
                                        .collection(COLLECTION_ASSINATURAS)
                                        .document(assinaturaId)
                                        .update("ativa", false)
                                        .get();

                        logger.log(
                                        Level.INFO,
                                        "Assinatura {0} cancelada com sucesso",
                                        assinaturaId);
                        return true;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(
                                        Level.SEVERE,
                                        "Erro ao cancelar assinatura " + assinaturaId,
                                        e);
                        throw new FirebaseOperationException(
                                        "Erro ao cancelar assinatura",
                                        e);
                } catch (Exception e) {
                        logger.log(
                                        Level.SEVERE,
                                        "Erro inesperado ao cancelar assinatura " + assinaturaId,
                                        e);
                        return false;
                }
        }

        /*
         * -----------------------------------------------------------
         * OUTRAS OPERAÇÕES
         * -----------------------------------------------------------
         */

        /**
         * Busca uma mensagem na coleção 'respostas'
         *
         * @param chave Chave da mensagem
         * @return Mensagem encontrada ou null
         */
        public static String buscarMensagem(String chave) {
                validateMessageKey(chave);
                checkInitialization();

                logger.log(Level.INFO, "Buscando mensagem com chave {0}", chave);

                try {
                        DocumentReference docRef = db
                                        .collection(COLLECTION_RESPOSTAS)
                                        .document(chave);
                        DocumentSnapshot document = docRef.get().get();

                        if (document.exists()) {
                                String mensagem = document.getString("mensagem");
                                logger.log(
                                                Level.INFO,
                                                "Mensagem com chave {0} encontrada",
                                                chave);
                                return mensagem;
                        }

                        logger.log(
                                        Level.INFO,
                                        "Mensagem com chave {0} não encontrada",
                                        chave);
                        return null;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(
                                        Level.SEVERE,
                                        "Erro ao buscar mensagem com chave " + chave,
                                        e);
                        return null;
                } catch (Exception e) {
                        logger.log(
                                        Level.SEVERE,
                                        "Erro inesperado ao buscar mensagem com chave " + chave,
                                        e);
                        return null;
                }
        }

        /**
         * Valida a chave da mensagem
         */
        private static void validateMessageKey(String chave) {
                if (chave == null || chave.trim().isEmpty()) {
                        throw new IllegalArgumentException(
                                        "Chave da mensagem não pode ser nula ou vazia");
                }
        }

        /**
         * Registra um novo evento no Firestore.
         * 
         * @param evento Objeto Evento com os dados a serem registrados
         * @return true se o evento foi registrado com sucesso
         * @throws IllegalArgumentException se o evento for nulo ou inválido
         */
        public static boolean registrarEvento(Evento evento) {
                if (evento == null) {
                        throw new IllegalArgumentException("Evento não pode ser nulo");
                }
                if (evento.getTipoEvento() == null || evento.getTipoEvento().trim().isEmpty()) {
                        throw new IllegalArgumentException("Tipo de evento é obrigatório");
                }
                // Garantir que a data/hora nunca seja nula
                if (evento.getDataHora() == null) {
                        evento.setDataHora(LocalDateTime.now());
                        logger.log(Level.INFO, "Evento sem data/hora definida, usando data atual");
                }

                checkInitialization();

                try {
                        Map<String, Object> data = new HashMap<>();
                        data.put("id", evento.getId());
                        data.put("tipoEvento", evento.getTipoEvento());
                        data.put("dataHora", Timestamp.of(
                                        Date.from(evento.getDataHora().atZone(ZoneId.systemDefault()).toInstant())));

                        // Adicione o ID do usuário se estiver disponível
                        if (evento.getUsuarioId() != null) {
                                data.put("usuarioId", evento.getUsuarioId());
                        }

                        db.collection(COLLECTION_EVENTOS)
                                        .document(evento.getId())
                                        .set(data)
                                        .get();

                        logger.log(Level.INFO, "Evento {0} registrado com sucesso", evento.getId());
                        return true;
                } catch (ExecutionException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                        logger.log(Level.SEVERE, "Erro ao registrar evento " + evento.getId(), e);
                        throw new FirebaseOperationException("Erro ao registrar evento", e);
                } catch (Exception e) {
                        logger.log(Level.SEVERE, "Erro inesperado ao registrar evento " + evento.getId(), e);
                        return false;
                }
        }

        /**
         * Busca eventos por ID do usuário
         * 
         * @param usuarioId ID do usuário
         * @return Lista de eventos do usuário
         * @throws ExecutionException   se houver erro na execução
         * @throws InterruptedException se a operação for interrompida
         */
        public static List<Evento> buscarEventosPorUsuario(String usuarioId)
                        throws ExecutionException, InterruptedException {

                validateUserId(usuarioId);
                checkInitialization();

                List<Evento> eventos = new ArrayList<>();

                QuerySnapshot querySnapshot = db.collection(COLLECTION_EVENTOS)
                                .whereEqualTo("usuarioId", usuarioId)
                                .get()
                                .get();

                for (QueryDocumentSnapshot document : querySnapshot) {
                        Evento evento = new Evento();
                        evento.setId(document.getString("id"));
                        evento.setTipoEvento(document.getString("tipoEvento"));
                        evento.setUsuarioId(document.getString("usuarioId"));

                        Timestamp timestamp = document.get("dataHora", Timestamp.class);
                        if (timestamp != null) {
                                evento.setDataHora(timestamp.toDate().toInstant()
                                                .atZone(ZoneId.systemDefault())
                                                .toLocalDateTime());
                        } else {
                                // Se a data/hora for nula, usar a data/hora atual como fallback
                                evento.setDataHora(LocalDateTime.now());
                                logger.log(Level.WARNING, "Evento {0} sem data/hora, usando data atual como fallback",
                                                document.getId());
                        }

                        eventos.add(evento);
                }

                return eventos;
        }

        /*
         * -----------------------------------------------------------
         * CLASSES DE EXCEÇÃO CUSTOMIZADAS
         * -----------------------------------------------------------
         */

        /**
         * Exceção lançada quando há erro na inicialização do Firebase
         */
        public static class FirebaseInitializationException
                        extends RuntimeException {

                public FirebaseInitializationException(
                                String message,
                                Throwable cause) {
                        super(message, cause);
                }
        }

        /**
         * Exceção lançada quando há erro em operações do Firebase
         */
        public static class FirebaseOperationException extends RuntimeException {

                public FirebaseOperationException(String message, Throwable cause) {
                        super(message, cause);
                }
        }
}

package io.github.edulanzarin.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;

import io.github.edulanzarin.models.Assinatura;
import io.github.edulanzarin.models.Pagamento;
import io.github.edulanzarin.models.Usuario;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
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
 * Todas as operações são estáticas e thread-safe.
 */
public class Firebase {
    private static final Logger logger = Logger.getLogger(Firebase.class.getName());
    private static Firestore db;

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
        initialize();
    }

    /**
     * Configura e inicializa a conexão com o Firebase Firestore.
     * Usa credenciais das variáveis de ambiente.
     * 
     * @throws RuntimeException se a inicialização falhar
     */
    public static void initialize() {
        try {
            if (db != null)
                return;

            JsonObject json = new JsonObject();
            json.addProperty("type", System.getenv("FIREBASE_TYPE"));
            json.addProperty("project_id", System.getenv("FIREBASE_PROJECT_ID"));
            json.addProperty("private_key_id", System.getenv("FIREBASE_PRIVATE_KEY_ID"));
            json.addProperty("private_key", System.getenv("FIREBASE_PRIVATE_KEY").replace("\\n", "\n"));
            json.addProperty("client_email", System.getenv("FIREBASE_CLIENT_EMAIL"));
            json.addProperty("client_id", System.getenv("FIREBASE_CLIENT_ID"));
            json.addProperty("auth_uri", System.getenv("FIREBASE_AUTH_URI"));
            json.addProperty("token_uri", System.getenv("FIREBASE_TOKEN_URI"));
            json.addProperty("auth_provider_x509_cert_url", System.getenv("FIREBASE_AUTH_PROVIDER_CERT_URL"));
            json.addProperty("client_x509_cert_url", System.getenv("FIREBASE_CLIENT_CERT_URL"));
            json.addProperty("universe_domain", System.getenv("FIREBASE_UNIVERSE_DOMAIN"));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(
                            new ByteArrayInputStream(json.toString().getBytes(StandardCharsets.UTF_8))))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.log(Level.INFO, "✅ Firebase inicializado com sucesso");
            }

            db = FirestoreClient.getFirestore();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Erro ao inicializar Firebase", e);
            throw new RuntimeException("Falha na inicialização do Firebase", e);
        }
    }

    /**
     * Verifica se o Firebase foi inicializado corretamente
     * 
     * @throws IllegalStateException se o Firestore não estiver inicializado
     */
    private static void checkInitialization() {
        if (db == null) {
            throw new IllegalStateException("Firebase não foi inicializado corretamente");
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
     */
    public static boolean verificarECadastrarUsuario(Usuario usuario) {
        checkInitialization();
        try {
            DocumentReference docRef = db.collection("usuarios").document(usuario.getId());

            if (docRef.get().get().exists()) {
                logger.log(Level.INFO, "Usuário {0} já existe", usuario.getId());
                return false;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("id", usuario.getId());
            data.put("usuario", usuario.getUsuario());
            data.put("nome", usuario.getNome());
            data.put("assinaturaId", null);

            docRef.set(data).get();
            logger.log(Level.INFO, "Usuário {0} cadastrado", usuario.getId());
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao cadastrar usuário", e);
            return false;
        }
    }

    /**
     * Busca um usuário pelo ID
     * 
     * @param usuarioId ID do usuário
     * @return Objeto Usuario ou null se não encontrado
     */
    public static Usuario buscarUsuario(String usuarioId) throws ExecutionException, InterruptedException {
        checkInitialization();
        DocumentSnapshot doc = db.collection("usuarios").document(usuarioId).get().get();

        if (!doc.exists()) {
            return null;
        }

        Usuario usuario = new Usuario(
                doc.getString("id"),
                doc.getString("usuario"),
                doc.getString("nome"));
        usuario.setAssinaturaId(doc.getString("assinaturaId"));
        return usuario;
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
     * @return ID do pagamento criado ou null em caso de erro
     */
    public static String criarPagamento(Pagamento pagamento) {
        checkInitialization();
        try {
            DocumentReference ref = db.collection("pagamentos").document(pagamento.getId());

            Map<String, Object> data = new HashMap<>();
            data.put("id", pagamento.getId());
            data.put("usuarioId", pagamento.getUsuarioId());
            data.put("vencimento", Timestamp.of(Date.from(
                    pagamento.getVencimento().atZone(ZoneId.systemDefault()).toInstant())));
            data.put("status", pagamento.getStatus());
            data.put("plano", pagamento.getPlano());
            data.put("valor", pagamento.getValor());

            ref.set(data).get();
            return ref.getId();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao criar pagamento", e);
            return null;
        }
    }

    /**
     * Confirma um pagamento e cria a assinatura correspondente
     * 
     * @param pagamentoId ID do pagamento a ser confirmado
     * @return true se a operação foi bem-sucedida
     */
    public static boolean confirmarPagamento(String pagamentoId) {
        checkInitialization();
        try {
            // 1. Atualizar status do pagamento
            db.collection("pagamentos").document(pagamentoId)
                    .update("status", "APROVADO");

            // 2. Buscar dados do pagamento
            DocumentSnapshot pagamentoDoc = db.collection("pagamentos")
                    .document(pagamentoId).get().get();

            if (!pagamentoDoc.exists()) {
                throw new IllegalArgumentException("Pagamento não encontrado");
            }

            String usuarioId = pagamentoDoc.getString("usuarioId");
            String plano = pagamentoDoc.getString("plano");

            if (usuarioId == null || plano == null) {
                throw new IllegalStateException("Dados incompletos no pagamento");
            }

            // 3. Criar nova assinatura
            DocumentReference assinaturaRef = db.collection("assinaturas").document();
            LocalDate inicio = LocalDate.now();

            Assinatura assinatura = new Assinatura(
                    assinaturaRef.getId(),
                    usuarioId,
                    pagamentoId,
                    inicio,
                    plano,
                    true);

            Map<String, Object> assinaturaData = new HashMap<>();
            assinaturaData.put("id", assinaturaRef.getId());
            assinaturaData.put("usuarioId", usuarioId);
            assinaturaData.put("pagamentoId", pagamentoId);
            assinaturaData.put("dataInicio", Timestamp.of(Date.from(
                    inicio.atStartOfDay(ZoneId.systemDefault()).toInstant())));
            assinaturaData.put("tipoPlano", plano);
            assinaturaData.put("ativa", true);
            assinaturaData.put("dataFim", Timestamp.of(Date.from(
                    assinatura.getDataFim().atStartOfDay(ZoneId.systemDefault()).toInstant())));

            assinaturaRef.set(assinaturaData).get();

            // 4. Atualizar usuário com ID da assinatura
            db.collection("usuarios").document(usuarioId)
                    .update("assinaturaId", assinaturaRef.getId());

            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao confirmar pagamento", e);
            return false;
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
     */
    public static Assinatura buscarAssinatura(String assinaturaId) throws ExecutionException, InterruptedException {
        checkInitialization();

        try {
            DocumentSnapshot doc = db.collection("assinaturas").document(assinaturaId).get().get();

            // Verifica se o documento existe e contém os campos obrigatórios
            if (!doc.exists() || !doc.contains("dataInicio") || !doc.contains("usuarioId") ||
                    !doc.contains("pagamentoId") || !doc.contains("ativa") || !doc.contains("tipoPlano")) {
                return null;
            }

            // Tratamento seguro para dataInicio
            Timestamp inicioTimestamp = doc.get("dataInicio", Timestamp.class);
            if (inicioTimestamp == null) {
                logger.log(Level.WARNING, "Data de início nula para assinatura: " + assinaturaId);
                return null;
            }
            LocalDate inicio = inicioTimestamp.toDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();

            // Tratamento para tipoPlano
            String tipoPlano = doc.getString("tipoPlano");
            if (tipoPlano == null) {
                logger.log(Level.WARNING, "Tipo de plano nulo para assinatura: " + assinaturaId);
                return null;
            }

            // Tratamento para o campo ativa
            Boolean ativa = doc.getBoolean("ativa");
            if (ativa == null) {
                logger.log(Level.WARNING, "Campo 'ativa' nulo para assinatura: " + assinaturaId);
                ativa = false;
            }

            // Usando o construtor que calcula a dataFim automaticamente
            return new Assinatura(
                    doc.getString("id"),
                    doc.getString("usuarioId"),
                    doc.getString("pagamentoId"),
                    inicio,
                    tipoPlano,
                    ativa);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar assinatura " + assinaturaId, e);
            return null;
        }
    }

    /**
     * Verifica se um usuário possui assinatura ativa
     * 
     * @param usuarioId ID do usuário
     * @return true se o usuário tem assinatura válida
     */
    public static boolean verificarAssinaturaAtiva(String usuarioId) throws ExecutionException, InterruptedException {
        checkInitialization();

        Usuario usuario = buscarUsuario(usuarioId);
        if (usuario == null || usuario.getAssinaturaId() == null) {
            return false;
        }

        Assinatura assinatura = buscarAssinatura(usuario.getAssinaturaId());
        if (assinatura == null) {
            return false;
        }

        LocalDate hoje = LocalDate.now();
        return assinatura.isAtiva() &&
                (assinatura.getDataFim() == null || hoje.isBefore(assinatura.getDataFim()));
    }

    /**
     * Cancela uma assinatura (marca como inativa)
     * 
     * @param assinaturaId ID da assinatura
     * @return true se a operação foi bem-sucedida
     */
    public static boolean cancelarAssinatura(String assinaturaId) {
        checkInitialization();
        try {
            db.collection("assinaturas").document(assinaturaId)
                    .update("ativa", false);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao cancelar assinatura " + assinaturaId, e);
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
        try {
            DocumentReference docRef = db.collection("respostas").document(chave);
            DocumentSnapshot document = docRef.get().get();

            if (document.exists()) {
                return document.getString("mensagem");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar mensagem do Firebase", e);
        }
        return null;
    }
}
package io.github.edulanzarin.services;

import com.google.api.core.ApiFuture;
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

/*
 * Classe de serviço responsável pela integração com o Firebase Firestore.
 * Esta classe realiza a configuração e inicialização do Firebase com base nas
 * variáveis de ambiente (.env) e fornece métodos para leitura e gravação de 
 * dados armazenados no Firestore.
 */
public class Firebase {
    private static final Logger logger = Logger.getLogger(Firebase.class.getName());
    private static Firestore db;

    /*
     * Bloco estático que é executado uma única vez quando a classe é carregada.
     * Chama o método initialize() para garantir que a conexão com o Firebase
     * seja configurada assim que a classe for usada pela primeira vez.
     */
    static {
        initialize();
    }

    /*
     * Inicializa a conexão com o Firebase Firestore usando as credenciais
     * fornecidas pelas variáveis de ambiente.
     * - Constrói um objeto JSON com as configurações necessárias.
     * - Cria as opções de configuração do Firebase.
     * - Inicializa o FirebaseApp se ainda não estiver inicializado.
     * - Obtém a instância do Firestore para uso posterior.
     * Caso já esteja inicializado, o método retorna imediatamente.
     * Em caso de erro, lança uma RuntimeException.
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

            // Criação das opções de configuração do Firebase
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
     * Verifica se o Firebase Firestore foi inicializado corretamente.
     * Retorna exceção caso o Firestore não tenha sido inicializado.
     */
    private static void checkInitialization() {
        if (db == null) {
            throw new IllegalStateException("Firebase não foi inicializado corretamente");
        }
    }

    /*
     * Busca uma mensagem no Firestore a partir da coleção 'respostas' usando a
     * chave (nome do documento).
     * Retorna a string da mensagem associada à chave, se encontrada.
     * Caso ocorra algum erro ou a chave não exista, retorna null.
     */
    public static String buscarMensagem(String chave) {
        try {
            DocumentReference docRef = db.collection("respostas").document(chave);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return document.getString("mensagem");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao buscar mensagem do Firebase:");
            e.printStackTrace();
        }

        return null;
    }

    /*
     * Este método verifica se um usuário já existe na coleção 'usuarios' do
     * Firestore.
     * Se o usuário não existir, ele será cadastrado com os dados fornecidos.
     * O campo "assinaturaId" é inicialmente definido como null.
     * Retorna true se o usuário foi cadastrado com sucesso.
     * Retorna false se o usuário já existia ou se ocorreu algum erro.
     */
    public static boolean verificarECadastrarUsuario(Usuario usuario) {
        // Garante que o Firebase esteja inicializado
        checkInitialization();
        try {
            // Referência ao documento do usuário
            DocumentReference docRef = db.collection("usuarios").document(usuario.getId());

            // Verifica se o usuário já existe
            if (docRef.get().get().exists()) {
                logger.log(Level.INFO, "Usuário {0} já existe", usuario.getId());
                return false;
            }

            // Mapeia os dados do novo usuário
            Map<String, Object> data = new HashMap<>();
            data.put("id", usuario.getId());
            data.put("usuario", usuario.getUsuario());
            data.put("nome", usuario.getNome());
            data.put("assinaturaId", null); // Inicialmente sem assinatura

            // Salva o novo usuário no Firestore
            docRef.set(data).get();
            logger.log(Level.INFO, "Usuário {0} cadastrado", usuario.getId());

            return true;
        } catch (Exception e) {
            // Em caso de erro, registra no log e retorna false
            logger.log(Level.SEVERE, "Erro ao cadastrar usuário", e);
            return false;
        }
    }

    /*
     * Este método cria um novo pagamento no Firestore.
     * Ele recebe um objeto `Pagamento`, converte seus dados em um mapa,
     * define o status inicial como "PENDENTE" e salva o documento na coleção
     * "pagamentos".
     * Retorna o ID do pagamento criado, ou null em caso de erro.
     */
    public static String criarPagamento(Pagamento pagamento) {
        // Garante que o Firebase esteja inicializado
        checkInitialization();
        try {
            // Cria uma referência ao documento na coleção "pagamentos" com o ID fornecido
            DocumentReference ref = db.collection("pagamentos").document(pagamento.getId());

            // Mapeia os dados do pagamento para um objeto Map
            Map<String, Object> data = new HashMap<>();
            data.put("id", pagamento.getId());
            data.put("usuarioId", pagamento.getUsuarioId());
            data.put("vencimento", Timestamp.of(Date.from(
                    pagamento.getVencimento().atZone(ZoneId.systemDefault()).toInstant())));
            data.put("status", "PENDENTE"); // Define status inicial como pendente

            // Salva o documento no Firestore
            ref.set(data).get();

            // Retorna o ID do documento criado
            return ref.getId();
        } catch (Exception e) {
            // Em caso de erro, registra no log e retorna null
            logger.log(Level.SEVERE, "Erro ao criar pagamento", e);
            return null;
        }
    }

    /*
     * Este método confirma o pagamento de um plano de assinatura.
     * Ele realiza as seguintes etapas:
     * 1. Atualiza o status do pagamento para "APROVADO".
     * 2. Busca os dados do pagamento e verifica se são válidos.
     * 3. Cria uma nova assinatura com base no plano escolhido e vincula ao usuário.
     * 4. Atualiza o documento do usuário com o ID da nova assinatura.
     * Retorna true se todas as operações forem bem-sucedidas, ou false em caso de
     * erro.
     */
    public static boolean confirmarPagamento(String pagamentoId) {
        // Garante que o Firebase esteja inicializado
        checkInitialization();
        try {
            // 1. Atualizar status do pagamento para "APROVADO"
            db.collection("pagamentos").document(pagamentoId)
                    .update("status", "APROVADO");

            // 2. Buscar dados do pagamento
            DocumentSnapshot pagamentoDoc = db.collection("pagamentos")
                    .document(pagamentoId).get().get();

            // Se o documento não existe, lançar exceção
            if (!pagamentoDoc.exists()) {
                throw new IllegalArgumentException("Pagamento não encontrado");
            }

            // Extrair informações do documento
            String usuarioId = pagamentoDoc.getString("usuarioId");
            String planoStr = pagamentoDoc.getString("plano");

            // Verifica se os dados essenciais estão presentes
            if (usuarioId == null || planoStr == null) {
                throw new IllegalStateException("Dados incompletos no pagamento");
            }

            // Define a data de início como hoje
            LocalDate inicio = LocalDate.now();

            // 3. Criar nova assinatura no Firestore
            DocumentReference assinaturaRef = db.collection("assinaturas").document();

            // Preencher os dados da assinatura
            Map<String, Object> assinaturaData = new HashMap<>();
            assinaturaData.put("id", assinaturaRef.getId());
            assinaturaData.put("usuarioId", usuarioId);
            assinaturaData.put("pagamentoId", pagamentoId);
            assinaturaData.put("dataInicio", Timestamp.of(Date.from(
                    inicio.atStartOfDay(ZoneId.systemDefault()).toInstant())));

            // Define a assinatura como ativa
            assinaturaData.put("ativa", true);
            // Salva a assinatura no Firestore
            assinaturaRef.set(assinaturaData).get();

            // 4. Atualiza o usuário com o ID da nova assinatura
            db.collection("usuarios").document(usuarioId)
                    .update("assinaturaId", assinaturaRef.getId());

            // Operação concluída com sucesso
            return true;
        } catch (Exception e) {
            // Em caso de erro, loga a exceção e retorna false
            logger.log(Level.SEVERE, "Erro ao confirmar pagamento", e);
            return false;
        }
    }

    /*
     * Método responsável por buscar um usuário no Firebase Firestore
     * com base no ID fornecido. Caso o documento exista, retorna um
     * objeto `Usuario` com os campos extraídos do banco.
     * Se o documento não existir, retorna `null`.
     * Requer tratamento de exceções `ExecutionException` e `InterruptedException`
     * por se tratar de uma operação assíncrona com o Firestore.
     */
    public static Usuario buscarUsuario(String usuarioId) throws ExecutionException, InterruptedException {
        checkInitialization(); // Garante que o Firebase foi inicializado

        // Busca o documento com o ID especificado
        DocumentSnapshot doc = db.collection("usuarios").document(usuarioId).get().get();

        // Se o documento existir, retorna um novo objeto Usuario; senão, retorna null
        return doc.exists() ? new Usuario(
                doc.getString("id"),
                doc.getString("usuario"),
                doc.getString("nome")) : null;
    }

    /*
     * Método responsável por buscar uma assinatura no Firebase Firestore
     * com base no ID fornecido. Ele garante a inicialização do Firebase,
     * faz a leitura segura dos campos do documento (com tratamento de campos nulos
     * ou ausentes)
     * e retorna um objeto `Assinatura` populado com os dados obtidos.
     * Se o documento não existir, estiver incompleto ou ocorrer um erro na leitura,
     * o método retorna `null`. Além disso, mensagens de erro e avisos são
     * registradas
     * no logger para facilitar a depuração.
     */
    public static Assinatura buscarAssinatura(String assinaturaId) throws ExecutionException, InterruptedException {
        checkInitialization();

        try {
            DocumentSnapshot doc = db.collection("assinaturas").document(assinaturaId).get().get();

            // Verifica se o documento existe e contém os campos obrigatórios
            if (!doc.exists() || !doc.contains("dataInicio") || !doc.contains("usuarioId") ||
                    !doc.contains("pagamentoId") || !doc.contains("ativa")) {
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

            // Tratamento opcional para dataFim
            LocalDate fim = null;
            if (doc.contains("dataFim")) {
                Timestamp fimTimestamp = doc.get("dataFim", Timestamp.class);
                if (fimTimestamp != null) {
                    fim = fimTimestamp.toDate().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                }
            }

            // Tratamento seguro para o campo ativa
            Boolean ativa = doc.getBoolean("ativa");
            if (ativa == null) {
                logger.log(Level.WARNING, "Campo 'ativa' nulo para assinatura: " + assinaturaId);
                ativa = false; // Valor padrão seguro
            }

            // Cria e retorna o objeto Assinatura com os dados extraídos
            return new Assinatura(
                    doc.getString("id"),
                    doc.getString("usuarioId"),
                    doc.getString("pagamentoId"),
                    inicio,
                    fim,
                    ativa);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar assinatura " + assinaturaId, e);
            return null;
        }
    }

}
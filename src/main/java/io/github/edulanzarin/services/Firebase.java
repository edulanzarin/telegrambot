package io.github.edulanzarin.services;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;

import io.github.edulanzarin.models.Assinatura;
import io.github.edulanzarin.models.Pagamento;
import io.github.edulanzarin.models.Usuario;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/*
 * Classe de serviço responsável pela integração com o Firebase Firestore.
 * Esta classe realiza a configuração e inicialização do Firebase com base nas
 * variáveis
 * de ambiente (.env) e fornece métodos para leitura de dados armazenados no
 * Firestore.
 */
public class Firebase {

    // Instância compartilhada do Firestore
    private static Firestore db;

    /*
     * Inicializa a conexão com o Firebase usando variáveis de ambiente.
     * As credenciais são carregadas em tempo de execução como um JSON construído
     * a partir das variáveis de ambiente definidas no sistema, sem a necessidade de
     * um arquivo físico.
     */
    public static void initialize() {
        try {
            // Construindo o JSON com as credenciais do Firebase
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

            // Converte o JSON para InputStream necessário pelo FirebaseOptions
            InputStream serviceAccount = new ByteArrayInputStream(
                    json.toString().getBytes(StandardCharsets.UTF_8));

            // Criação das opções de configuração do Firebase
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Inicializa o app apenas se ainda não estiver ativo
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase inicializado com variáveis de ambiente.");
            } else {
                System.out.println("⚠️ Firebase já estava inicializado.");
            }

            // Obtém a instância do Firestore
            db = FirestoreClient.getFirestore();

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar o Firebase:");
            e.printStackTrace();
        }
    }

    /*
     * Busca uma mensagem do Firestore na coleção 'respostas' pelo nome do documento
     * (chave).
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

    /**
     * Verifica se um usuário existe na coleção 'usuarios' pelo ID.
     * Se não existir, cadastra o novo usuário.
     */
    public static boolean verificarECadastrarUsuario(Usuario usuario) {
        try {
            // Verifica se o usuário já existe
            DocumentReference docRef = db.collection("usuarios").document(usuario.getId());
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                System.out.println("Usuário " + usuario.getId() + " já existe na base de dados.");
                return false;
            }

            /*
             * Se o usuário não existir, monta o JSON a partir dos dados do objeto
             * Usuário e faz o cadastro no Firestore.
             * A assinatura começa inicialmente null.
             */
            Map<String, Object> usuarioData = new HashMap<>();
            usuarioData.put("id", usuario.getId());
            usuarioData.put("usuario", usuario.getUsuario());
            usuarioData.put("nome", usuario.getNome());
            usuarioData.put("assinatura", null);

            // Cadastra o usuário no Firestore
            ApiFuture<WriteResult> writeResult = docRef.set(usuarioData);
            writeResult.get(); // Aguarda a operação ser concluída

            System.out.println("Usuário " + usuario.getId() + " cadastrado com sucesso.");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erro ao verificar/cadastrar usuário:");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean cadastrarAssinatura(Usuario usuario, Assinatura assinatura) {
        return false;
    }

    public static boolean cadastrarPagamento(Usuario usuario, Assinatura assinatura, Pagamento pagamento) {
        return false;
    }
}

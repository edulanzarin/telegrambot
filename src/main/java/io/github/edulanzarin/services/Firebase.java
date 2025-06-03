package io.github.edulanzarin.services;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Firebase {

    private static Firestore db;

    public static void initialize() {
        try {
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

            InputStream serviceAccount = new ByteArrayInputStream(
                    json.toString().getBytes(StandardCharsets.UTF_8));

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase inicializado com variáveis de ambiente.");
            } else {
                System.out.println("⚠️ Firebase já estava inicializado.");
            }

            db = FirestoreClient.getFirestore();

        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar o Firebase:");
            e.printStackTrace();
        }
    }

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
}

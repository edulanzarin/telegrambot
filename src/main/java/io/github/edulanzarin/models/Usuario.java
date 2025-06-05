package io.github.edulanzarin.models;

/*
 * Classe responsável pelo modelo do usuário, onde as informações do usuários
 * serão coletadas pelo próprio bot no Telegram e a assinaturaId será
 * atribuída como null sempre que um usuário for criado.
 * Cada usuário pode ter apenas uma assinatura ativa, sendo necessário um controle
 * de vencimento das assinaturas, permitindo que o usuário tenha privilégios VIP
 * mesmo um dia após o vencimento (tempo para enviar mensagem de renovação e assinatura).
 */
public class Usuario {

    private String id;
    private String usuario; // username do Telegram
    private String nome; // nome completo do usuário
    private String assinaturaId; // ID da assinatura ativa (pode ser null)

    /*
     * Construtor padrão sem assinatura, pois ela será inserida apenas quando
     * o usuário realizar uma compra e o pagamento for confirmado.
     */
    public Usuario(String id, String usuario, String nome) {
        this.id = id;
        this.usuario = usuario;
        this.nome = nome;
        this.assinaturaId = null; // Explicitamente null
    }

    // Getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAssinaturaId() {
        return assinaturaId;
    }

    public void setAssinaturaId(String assinaturaId) {
        this.assinaturaId = assinaturaId;
    }

    /**
     * Método auxiliar para verificar se o usuário tem uma assinatura ativa
     * (considerando que a assinatura pode estar expirada mas ainda dentro do
     * período de cortesia)
     */
    public boolean temAssinaturaAtiva() {
        return assinaturaId != null && !assinaturaId.isEmpty();
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", usuario=" + usuario + ", nome=" + nome + ", assinaturaId=" + assinaturaId + "]";
    }
}
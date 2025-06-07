package io.github.edulanzarin.models;

/**
 * Representa um usuário cadastrado proveniente de uma interação
 * com o bot do Telegram. Cada usuário pode ter no máximo uma assinatura ativa,
 * identificada por um ID de assinatura.
 * 
 * A associação com a assinatura ocorre após a confirmação de um pagamento.
 */
public class Usuario {

    private String id; // ID único do usuário
    private String usuario; // Nome de usuário no Telegram
    private String nome; // Nome completo do usuário
    private String assinaturaId; // ID da assinatura ativa (null como padrão)

    /**
     * Cria uma nova instância de {@code Usuario} sem assinatura associada
     * inicialmente.
     * 
     * @param id      Identificador único do usuário
     * @param usuario Nome de usuário no Telegram
     * @param nome    Nome completo do usuário
     */
    public Usuario(String id, String usuario, String nome) {
        this.id = id;
        this.usuario = usuario;
        this.nome = nome;
        this.assinaturaId = null;
    }

    // Getters e setters padrão
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
     * Verifica se o usuário possui uma assinatura ativa associada.
     * 
     * @return {@code true} se houver uma assinatura ativa; {@code false} caso
     *         contrário
     */
    public boolean temAssinaturaAtiva() {
        return assinaturaId != null && !assinaturaId.isEmpty();
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", usuario=" + usuario + ", nome=" + nome + ", assinaturaId=" + assinaturaId + "]";
    }
}

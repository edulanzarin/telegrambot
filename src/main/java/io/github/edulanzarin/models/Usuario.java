package io.github.edulanzarin.models;

/*
 * Classe responsável pelo modelo do usuário, onde as informações do usuários
 * serão coletadas pelo próprio bot no Telegram e a assinatura será
 * atribuída como null sempre que um usuário for criado.
 * Cada usuário pode ter apenas uma assinatura, sendo necessário um controle
 * de vencimento das assinaturas, permitindo que o usuário tenha privilégios VIP
 * mesmo um dia após o vencimento (tempo para enviar mensagem de renovação e assinatura).
 */
public class Usuario {

    private String id;
    private String usuario;
    private String nome;
    private Assinatura assinatura;

    /*
     * Construtor padrão sem assinatura, pois ela será inserida apenas quando
     * o usuário realizar uma compra.
     */
    public Usuario(String id, String usuario, String nome) {
        this.id = id;
        this.usuario = usuario;
        this.nome = nome;
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

    public Assinatura getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(Assinatura assinatura) {
        this.assinatura = assinatura;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", usuario=" + usuario + ", nome=" + nome + ", assinatura=" + assinatura + "]";
    }
}

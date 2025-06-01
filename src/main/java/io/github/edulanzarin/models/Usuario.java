package io.github.edulanzarin.models;

/*
 * Classe responsável pelo modelo do usuário, onde as informações do usuários
 * serão coletadas pelo próprio bot no Telegram e a assinatura será
 * atribuída como null sempre que um usuário for criado.
 */
public class Usuario {

    private String id;
    private String usuario;
    private String nome;
    private Assinatura assinatura;

    /*
     * Getters e setters
     */
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

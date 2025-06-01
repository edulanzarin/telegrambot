package io.github.edulanzarin.utils;

import io.github.edulanzarin.models.Usuario;
import io.github.edulanzarin.services.Firebase;

/*
 * Classe utilitário responsável pelas respostas ao usuário.
 * Todas as respostas estão armazenadas no Firebase e serão usadas
 * dependendo do contexto da interação.
 */
public class Resposta {

    /*
     * SERÁ ENVIADO UMA SEQUÊNCIA DE QUATRO MENSAGENS INICIAIS PARA O USUÁRIO
     * 
     * Mensagem de bem-vindo que será enviada ao usuário na primeira interação
     * ou com o comando /start.
     */
    public String bemVindo(Usuario usuario) {
        String mensagem = Firebase.buscarMensagem("bem_vindo");

        return String.format(mensagem, usuario.getNome());
    }

    /*
     * Vídeo inicial enviado junto com a mensagem de bem-vindo
     */
    public String videoInicial() {
        String mensagem = Firebase.buscarMensagem("video_inicial");

        return mensagem;
    }

    /*
     * Informações sobre o grupo vip, também enviado junto com a mensagem de
     * bem-vindo
     */
    public String informacoesGrupo() {
        String mensagem = Firebase.buscarMensagem("informacoes_grupo");

        return mensagem;
    }

    /*
     * Botões para selecionar o plano, enviado junto com a mensagem de bem-vindo,
     * mas também em outros momentos.
     */
    public String botoesPlanos() {
        String mensagem = Firebase.buscarMensagem("botoes_planos");

        return mensagem;
    }
}

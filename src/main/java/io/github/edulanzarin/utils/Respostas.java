package io.github.edulanzarin.utils;

import io.github.edulanzarin.models.Usuario;
import io.github.edulanzarin.services.FirebaseService;

public class Respostas {
    private static Respostas instance;

    private Respostas() {
    } // Construtor privado para singleton

    public static Respostas getInstance() {
        if (instance == null) {
            instance = new Respostas();
        }
        return instance;
    }

    // Mensagens gerais
    public String bemVindo(Usuario usuario) {
        return formatarMensagem("bem_vindo", usuario.getNome());
    }

    public String videoInicial() {
        return buscarMensagemDireta("video_inicial");
    }

    public String informacoesGrupo() {
        return buscarMensagemDireta("informacoes_grupo");
    }

    public String botoesPlanos() {
        return buscarMensagemDireta("botoes_planos");
    }

    // Mensagens de comando
    public String comandoStart(Usuario usuario) {
        return bemVindo(usuario);
    }

    public String comandoHelp() {
        return buscarMensagemDireta("help");
    }

    public String comandoAssinatura() {
        return buscarMensagemDireta("assinatura");
    }

    public String comandoNaoReconhecido() {
        return buscarMensagemDireta("comando_nao_reconhecido");
    }

    public String mensagemPadrao() {
        return buscarMensagemDireta("mensagem_padrao");
    }

    // Métodos auxiliares
    private String buscarMensagemDireta(String chave) {
        String mensagem = FirebaseService.buscarMensagem(chave);
        return mensagem != null ? mensagem : "Mensagem não configurada: " + chave;
    }

    private String formatarMensagem(String chave, Object... args) {
        String mensagem = FirebaseService.buscarMensagem(chave);
        return mensagem != null ? String.format(mensagem, args) : "Mensagem não configurada: " + chave;
    }
}
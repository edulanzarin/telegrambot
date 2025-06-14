package io.github.edulanzarin.handlers;

import io.github.edulanzarin.models.Usuario;
import io.github.edulanzarin.utils.Respostas;

public class ProcessamentoComandosHandler {
    private final Respostas respostas;

    public ProcessamentoComandosHandler() {
        this.respostas = Respostas.getInstance();
    }

    public String processarComando(String comando, Usuario usuario) {
        return switch (comando) {
            case "/start" -> respostas.comandoStart(usuario);
            case "/help" -> respostas.comandoHelp();
            default -> respostas.comandoNaoReconhecido();
        };
    }
}
package br.com.alura.forumhub.domain.topico;

public record DadosAtualizacaoTopico(
        String titulo,
        String mensagem,
        String status
) {}

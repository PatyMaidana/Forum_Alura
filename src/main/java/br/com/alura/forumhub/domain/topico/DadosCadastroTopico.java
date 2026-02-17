package br.com.alura.forumhub.domain.topico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroTopico (
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotBlank String status,
        @NotNull Long autor_id,
        @NotNull Long curso_id
) {}


package br.com.alura.forumhub.domain.autor;

public record DadosAutor(Long id, String nome, String email, String senha, Long perfilId) {
     public DadosAutor(Autor autor) {
        this(autor.getId(), autor.getNome(), autor.getEmail(), autor.getSenha(), autor.getPerfil().getId());
    }
}

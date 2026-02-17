package br.com.alura.forumhub.domain.topico;

import br.com.alura.forumhub.domain.autor.Autor;
import br.com.alura.forumhub.domain.curso.Curso;
import br.com.alura.forumhub.domain.resposta.Resposta;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Topico")
@Table(name = "topicos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensagem;
    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    private String status;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resposta> respostas = new ArrayList<>();

    public Topico(DadosCadastroTopico dados, Autor autor, Curso curso) {
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.status = dados.status();
        this.dataCriacao = LocalDateTime.now();
        this.autor = autor;
        this.curso = curso;
    }

    public void atualizarDados(DadosAtualizacaoTopico dados) {
        if (dados.titulo() != null) {
            this.titulo = dados.titulo();
        }
        if (dados.mensagem() != null) {
            this.mensagem = dados.mensagem();
        }
        if (dados.status() != null) {
            this.status = dados.status();
        }
    }
}

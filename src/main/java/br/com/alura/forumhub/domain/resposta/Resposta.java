package br.com.alura.forumhub.domain.resposta;

import br.com.alura.forumhub.domain.autor.Autor;
import br.com.alura.forumhub.domain.topico.Topico;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Resposta")
@Table(name = "respostas")
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mensagem", nullable = false)
    private String mensagem;
    private Boolean solucao;

    @ManyToOne
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

}

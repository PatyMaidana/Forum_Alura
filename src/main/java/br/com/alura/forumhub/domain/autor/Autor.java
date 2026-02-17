package br.com.alura.forumhub.domain.autor;

import br.com.alura.forumhub.domain.perfil.Perfil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Autor")
@Table(name = "autores")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    @JsonIgnore
    private String senha;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    public Autor(DadosAutor dados, Perfil perfil) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.senha = dados.senha();
        this.perfil = perfil;
    }


}

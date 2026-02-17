package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.domain.curso.Curso;
import br.com.alura.forumhub.domain.curso.CursoRepository;
import br.com.alura.forumhub.domain.curso.DadosCurso;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;
    @GetMapping("/{id}")
    public ResponseEntity buscaAutor (@PathVariable Long id) {
        var curso = cursoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosCurso(curso));
    }
    @GetMapping
    public ResponseEntity<Page<DadosCurso>> listarCursos(@PageableDefault(sort = {"nome"}) Pageable pageable) {
        var pagina = cursoRepository.findAll(pageable).map(DadosCurso::new);
        return ResponseEntity.ok(pagina);
    }

    @PostMapping
    public Curso cadastrar(@RequestBody @Valid Curso a){
        return cursoRepository.save(a);
    }

    @PutMapping()
    public Curso editar (@RequestBody @Valid Curso a){
        return cursoRepository.save(a);
    }
    @DeleteMapping("/{id}")
    @Transactional
    public void remover (@PathVariable Long id){
        cursoRepository.deleteById(id);
    }

}

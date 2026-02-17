package br.com.alura.forumhub.controller;

import br.com.alura.forumhub.domain.autor.AutorRepository;
import br.com.alura.forumhub.domain.curso.CursoRepository;
import br.com.alura.forumhub.domain.topico.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder builder) {
        var autor = autorRepository.getReferenceById(dados.autor_id());
        var curso = cursoRepository.getReferenceById(dados.curso_id());

        var topico = new Topico(dados, autor, curso);
        topicoRepository.save(topico);

        var uri = builder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarTopico(@PathVariable Long id){
        var topico = topicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoTopico>> listarTopicos(@PageableDefault(sort = {"titulo"}) Pageable pageable){
        var pagina = topicoRepository.findAll(pageable).map(DadosDetalhamentoTopico::new);
        return ResponseEntity.ok(pagina);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarTopico(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados) {
        var topico = topicoRepository.getReferenceById(id);
        topico.atualizarDados(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluirTopico(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

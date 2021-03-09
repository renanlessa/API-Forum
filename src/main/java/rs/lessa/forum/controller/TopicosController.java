package rs.lessa.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rs.lessa.forum.controller.dto.TopicoDetalhe;
import rs.lessa.forum.controller.dto.TopicoDto;
import rs.lessa.forum.controller.request.TopicoRequest;
import rs.lessa.forum.controller.request.UpdateTopicoRequest;
import rs.lessa.forum.modelo.Curso;
import rs.lessa.forum.modelo.Topico;
import rs.lessa.forum.repository.CursoRepository;
import rs.lessa.forum.repository.TopicoRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaTopicos")
    public Page<TopicoDto> list(@RequestParam(required = false) String nomeCurso,
                                @PageableDefault(sort="id") Pageable pageable) {

        //Pageable pageable = PageRequest.of(page, qtd, Sort.Direction.ASC, sort);

        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(pageable);
            return TopicoDto.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, pageable);
            return TopicoDto.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoRequest request, UriComponentsBuilder uriBuilder) {
        Curso curso = cursoRepository.findByNome(request.getNomeCurso());
        Topico topico = request.converter(curso);
        topicoRepository.save(topico);

        URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<TopicoDetalhe> detalhar(@PathVariable Long id) {
        Optional<Topico> byId = topicoRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok(new TopicoDetalhe(byId.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid UpdateTopicoRequest request) {
        Optional<Topico> byId = topicoRepository.findById(id);
        if (byId.isPresent()) {
            Topico topico = byId.get();

            topico.setTitulo(request.getTitulo());
            topico.setMensagem(request.getMensagem());

            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaTopicos", allEntries = true)
    public ResponseEntity remover(@PathVariable Long id) {
        Optional<Topico> byId = topicoRepository.findById(id);
        if (byId.isPresent()) {
            topicoRepository.deleteById(byId.get().getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
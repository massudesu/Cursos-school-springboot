package org.dt2.cursos.controllers;

import org.dt2.cursos.entities.Cursos;
import org.dt2.cursos.entities.Escolas;
import org.dt2.cursos.repositories.CursosRepository;
import org.dt2.cursos.repositories.EscolasRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursosController {
    private final CursosRepository repository;
    private final EscolasRepository escolasRepository;

    public CursosController(CursosRepository repository, EscolasRepository escolasRepository) {
        this.repository = repository;
        this.escolasRepository = escolasRepository;
    }

    @GetMapping
    public List<Cursos> mostrar() {
        return repository.findAll();
    }

    @PostMapping
    public Cursos criar(@RequestBody Cursos c) {
        vincularEscola(c);
        return repository.save(c);
    }

    @PutMapping("/{id}")
    public Cursos atualizar(@PathVariable int id, @RequestBody Cursos dados) {
        Cursos atual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado."));
        atual.setNome(dados.getNome());
        atual.setDescricao(dados.getDescricao());
        atual.setCargaHoraria(dados.getCargaHoraria());
        if (dados.getEscolaId() != null) {
            Escolas escola = escolasRepository.findById(dados.getEscolaId())
                    .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada."));
            atual.setEscola(escola);
        }
        return repository.save(atual);
    }

    private void vincularEscola(Cursos registro) {
        if (registro.getEscolaId() == null) {
            throw new IllegalArgumentException("Selecione uma escola.");
        }
        Escolas escola = escolasRepository.findById(registro.getEscolaId())
                .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada."));
        registro.setEscola(escola);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable int id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado.");
            }
            repository.deleteById(id);
            repository.flush();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir este registro porque ele está vinculado a outros registros. Remova primeiro os vínculos relacionados.", e);
        }
    }

    @GetMapping("/{id}")
    public Cursos buscarid(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado."));
    }
}

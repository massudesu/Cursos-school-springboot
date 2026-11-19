package org.dt2.cursos.controllers;

import org.dt2.cursos.entities.Professores;
import org.dt2.cursos.entities.Escolas;
import org.dt2.cursos.repositories.ProfessoresRepository;
import org.dt2.cursos.repositories.EscolasRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/professores")
public class ProfessoresController {
    private final ProfessoresRepository repository;
    private final EscolasRepository escolasRepository;

    public ProfessoresController(ProfessoresRepository repository, EscolasRepository escolasRepository) {
        this.repository = repository;
        this.escolasRepository = escolasRepository;
    }

    @GetMapping
    public List<Professores> mostrar() {
        return repository.findAll();
    }

    @PostMapping
    public Professores criar(@RequestBody Professores p) {
        vincularEscola(p);
        return repository.save(p);
    }

    @PutMapping("/{id}")
    public Professores atualizar(@PathVariable int id, @RequestBody Professores dados) {
        Professores atual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado."));
        atual.setNome(dados.getNome());
        atual.setEmail(dados.getEmail());
        atual.setEspecialidade(dados.getEspecialidade());
        atual.setTelefone(dados.getTelefone());
        if (dados.getEscolaId() != null) {
            Escolas escola = escolasRepository.findById(dados.getEscolaId())
                    .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada."));
            atual.setEscola(escola);
        }
        return repository.save(atual);
    }

    private void vincularEscola(Professores registro) {
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
    public Professores buscarid(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado."));
    }
}

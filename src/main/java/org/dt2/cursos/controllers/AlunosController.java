package org.dt2.cursos.controllers;

import org.dt2.cursos.entities.Alunos;
import org.dt2.cursos.entities.Escolas;
import org.dt2.cursos.repositories.AlunosRepository;
import org.dt2.cursos.repositories.EscolasRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunosController {
    private final AlunosRepository repository;
    private final EscolasRepository escolasRepository;

    public AlunosController(AlunosRepository repository, EscolasRepository escolasRepository) {
        this.repository = repository;
        this.escolasRepository = escolasRepository;
    }

    @GetMapping
    public List<Alunos> mostrar() {
        return repository.findAll();
    }

    @PostMapping
    public Alunos criar(@RequestBody Alunos a) {
        vincularEscola(a);
        return repository.save(a);
    }

    @PutMapping("/{id}")
    public Alunos atualizar(@PathVariable int id, @RequestBody Alunos a) {
        Alunos atual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado."));
        atual.setNome(a.getNome());
        atual.setEmail(a.getEmail());
        atual.setMatricula(a.getMatricula());
        atual.setTelefone(a.getTelefone());
        atual.setDataNascimento(a.getDataNascimento());
        if (a.getEscolaId() != null) {
            Escolas escola = escolasRepository.findById(a.getEscolaId())
                    .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada."));
            atual.setEscola(escola);
        }
        return repository.save(atual);
    }

    private void vincularEscola(Alunos registro) {
        if (registro.getEscolaId() == null) {
            throw new IllegalArgumentException("Selecione uma escola.");
        }
        Escolas escola = escolasRepository.findById(registro.getEscolaId())
                .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada."));
        registro.setEscola(escola);
    }

    @DeleteMapping("/{id}")
    public void DELETE(@PathVariable int id) {
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
}

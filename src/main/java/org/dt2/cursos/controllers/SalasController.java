package org.dt2.cursos.controllers;

import org.dt2.cursos.entities.Salas;
import org.dt2.cursos.entities.Escolas;
import org.dt2.cursos.repositories.SalasRepository;
import org.dt2.cursos.repositories.EscolasRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/salas")
public class SalasController {
    private final SalasRepository repository;
    private final EscolasRepository escolasRepository;

    public SalasController(SalasRepository repository, EscolasRepository escolasRepository) {
        this.repository = repository;
        this.escolasRepository = escolasRepository;
    }

    @GetMapping
    public List<Salas> mostrar() {
        return repository.findAll();
    }

    @PostMapping
    public Salas criar(@RequestBody Salas s) {
        vincularEscola(s);
        return repository.save(s);
    }

    @PutMapping("/{id}")
    public Salas atualizar(@PathVariable int id, @RequestBody Salas dados) {
        Salas atual = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado."));
        atual.setNome(dados.getNome());
        atual.setCapacidade(dados.getCapacidade());
        atual.setLocalizacao(dados.getLocalizacao());
        if (dados.getEscolaId() != null) {
            Escolas escola = escolasRepository.findById(dados.getEscolaId())
                    .orElseThrow(() -> new IllegalArgumentException("Escola não encontrada."));
            atual.setEscola(escola);
        }
        return repository.save(atual);
    }

    private void vincularEscola(Salas registro) {
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
    public Salas buscarid(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado."));
    }
}

package org.dt2.cursos.controllers;

import org.dt2.cursos.entities.*;
import org.dt2.cursos.repositories.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/turmas")
public class TurmasController {
    private final TurmasRepository repository;
    private final CursosRepository cursosRepository;
    private final ProfessoresRepository professoresRepository;
    private final SalasRepository salasRepository;

    public TurmasController(TurmasRepository repository, CursosRepository cursosRepository,
                            ProfessoresRepository professoresRepository, SalasRepository salasRepository) {
        this.repository = repository;
        this.cursosRepository = cursosRepository;
        this.professoresRepository = professoresRepository;
        this.salasRepository = salasRepository;
    }

    @GetMapping
    public List<Turmas> mostrar() {
        return repository.findAll();
    }

    @PostMapping
    public Turmas criarTurmas(@RequestBody Turmas turma) {
        vincularRelacionamentos(turma);
        return repository.save(turma);
    }

    @PutMapping("/{id}")
    public Turmas atualizar(@PathVariable int id, @RequestBody Turmas dados) {
        Turmas turma = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
        turma.setNome(dados.getNome());
        turma.setAnoLetivo(dados.getAnoLetivo());
        turma.setTurno(dados.getTurno());
        vincularRelacionamentos(dados);
        turma.setCurso(dados.getCurso());
        turma.setProfessor(dados.getProfessor());
        turma.setSala(dados.getSala());
        return repository.save(turma);
    }

    private void vincularRelacionamentos(Turmas turma) {
        if (turma.getCursoId() == null || turma.getProfessorId() == null || turma.getSalaId() == null) {
            throw new IllegalArgumentException("Selecione um curso, um professor e uma sala.");
        }
        Cursos curso = cursosRepository.findById(turma.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado."));
        Professores professor = professoresRepository.findById(turma.getProfessorId())
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado."));
        Salas sala = salasRepository.findById(turma.getSalaId())
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada."));
        turma.setCurso(curso);
        turma.setProfessor(professor);
        turma.setSala(sala);
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
    public Turmas buscarid(@PathVariable int id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
    }
}

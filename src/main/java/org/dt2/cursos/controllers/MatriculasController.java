package org.dt2.cursos.controllers;

import org.dt2.cursos.entities.*;
import org.dt2.cursos.repositories.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculasController {
    private final MatriculasRepository matriculasRepository;
    private final AlunosRepository alunosRepository;
    private final TurmasRepository turmasRepository;

    public MatriculasController(MatriculasRepository matriculasRepository,
                                AlunosRepository alunosRepository,
                                TurmasRepository turmasRepository) {
        this.matriculasRepository = matriculasRepository;
        this.alunosRepository = alunosRepository;
        this.turmasRepository = turmasRepository;
    }

    @GetMapping
    public List<Map<String, Object>> listar() {
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Matriculas matricula : matriculasRepository.findAll()) {
            Alunos aluno = matricula.getAluno();
            Turmas turma = matricula.getTurma();
            if (aluno == null || turma == null) continue;

            Cursos curso = turma.getCurso();
            Professores professor = turma.getProfessor();
            Escolas escola = curso != null ? curso.getEscola() : null;
            if (escola == null) escola = aluno.getEscola();

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", matricula.getId());
            item.put("alunoId", aluno.getId());
            item.put("alunoNome", aluno.getNome());
            item.put("matricula", aluno.getMatricula());
            item.put("turmaId", turma.getId());
            item.put("turmaNome", turma.getNome());
            item.put("cursoId", curso != null ? curso.getId() : null);
            item.put("cursoNome", curso != null ? curso.getNome() : "—");
            item.put("escolaId", escola != null ? escola.getId() : null);
            item.put("escolaNome", escola != null ? escola.getNome() : "—");
            item.put("professorId", professor != null ? professor.getId() : null);
            item.put("professorNome", professor != null ? professor.getNome() : "—");
            item.put("anoLetivo", turma.getAnoLetivo());
            item.put("turno", turma.getTurno());
            item.put("status", matricula.getStatus());
            resultado.add(item);
        }
        return resultado;
    }

    @PostMapping
    public Matriculas criar(@RequestBody Map<String, Object> dados) {
        Integer alunoId = Integer.valueOf(dados.get("alunoId").toString());
        Integer turmaId = Integer.valueOf(dados.get("turmaId").toString());
        Alunos aluno = alunosRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));
        Turmas turma = turmasRepository.findById(turmaId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada."));
        LocalDate data = dados.get("dataMatricula") != null
                ? LocalDate.parse(dados.get("dataMatricula").toString()) : LocalDate.now();
        String status = dados.get("status") != null ? dados.get("status").toString() : "Ativa";
        return matriculasRepository.save(new Matriculas(aluno, turma, data, status));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        try {
            if (!matriculasRepository.existsById(id)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado.");
            }
            matriculasRepository.deleteById(id);
            matriculasRepository.flush();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível excluir este registro porque ele está vinculado a outros registros. Remova primeiro os vínculos relacionados.", e);
        }
    }
}

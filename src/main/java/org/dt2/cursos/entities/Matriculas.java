package org.dt2.cursos.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Matriculas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Alunos aluno;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private Turmas turma;

    private LocalDate dataMatricula;

    private String status;

    public Matriculas() {}

    public Matriculas(Long id, Alunos aluno, Turmas turma, LocalDate dataMatricula, String status) {
        this.id = id;
        this.aluno = aluno;
        this.turma = turma;
        this.dataMatricula = dataMatricula;
        this.status = status;
    }

    public Matriculas(Alunos aluno, Turmas turma, LocalDate dataMatricula, String status) {
        this.aluno = aluno;
        this.turma = turma;
        this.dataMatricula = dataMatricula;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Alunos getAluno() {
        return aluno;
    }

    public void setAluno(Alunos aluno) {
        this.aluno = aluno;
    }

    public Turmas getTurma() {
        return turma;
    }

    public void setTurma(Turmas turma) {
        this.turma = turma;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
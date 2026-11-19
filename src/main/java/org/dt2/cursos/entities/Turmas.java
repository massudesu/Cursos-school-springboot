package org.dt2.cursos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Turmas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private int anoLetivo;
    private String turno;
    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Cursos curso;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professores professor;

    @ManyToOne
    @JoinColumn(name = "sala_id")
    private Salas sala;
    @JsonIgnore
    @OneToMany(mappedBy = "turma", cascade = CascadeType.REMOVE)
    private List<Matriculas> matriculas = new ArrayList<>();

    public Turmas(int id, String nome, int anoLetivo, String turno, Cursos curso, Professores professor, Salas sala) {
        this.id = id;
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.turno = turno;
        this.curso = curso;
        this.professor = professor;
        this.sala = sala;
    }

    public Turmas(String nome, int anoLetivo, String turno, Cursos curso, Professores professor, Salas sala) {
        this.nome = nome;
        this.anoLetivo = anoLetivo;
        this.turno = turno;
        this.curso = curso;
        this.professor = professor;
        this.sala = sala;
    }
    public Turmas(){}

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @JsonIgnore
    public Cursos getCurso() {
        return curso;
    }

    public void setCurso(Cursos curso) {
        this.curso = curso;
    }

    @JsonIgnore
    public Professores getProfessor() {
        return professor;
    }

    public void setProfessor(Professores professor) {
        this.professor = professor;
    }

    @JsonIgnore
    public Salas getSala() {
        return sala;
    }

    public void setSala(Salas sala) {
        this.sala = sala;
    }

    @JsonProperty("cursoId")
    public Integer getCursoId() {
        return curso != null ? curso.getId() : null;
    }

    @JsonProperty("cursoId")
    public void setCursoId(Integer cursoId) {
        this.curso = cursoId != null ? new Cursos(cursoId, null, null, 0) : null;
    }

    @JsonProperty("professorId")
    public Integer getProfessorId() {
        return professor != null ? professor.getId() : null;
    }

    @JsonProperty("professorId")
    public void setProfessorId(Integer professorId) {
        this.professor = professorId != null ? new Professores(professorId, null, null, null, null) : null;
    }

    @JsonProperty("salaId")
    public Integer getSalaId() {
        return sala != null ? sala.getId() : null;
    }

    @JsonProperty("salaId")
    public void setSalaId(Integer salaId) {
        this.sala = salaId != null ? new Salas(salaId, null, 0, null) : null;
    }

    public int getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(int anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}

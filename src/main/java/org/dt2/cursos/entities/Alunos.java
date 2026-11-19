package org.dt2.cursos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Alunos {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escolas escola;
    @JsonIgnore


    public Alunos(int id, String nome, String email, String telefone, LocalDate dataNascimento, String matricula) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.matricula = matricula;
    }

    public Alunos(String nome, String email, String telefone, LocalDate dataNascimento, String matricula) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.matricula = matricula;
    }
    public Alunos(){}

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @JsonIgnore
    public Escolas getEscola() {
        return escola;
    }

    public void setEscola(Escolas escola) {
        this.escola = escola;
    }

    @JsonProperty("escolaId")
    public Integer getEscolaId() {
        return escola != null ? escola.getId() : null;
    }

    @JsonProperty("escolaId")
    public void setEscolaId(Integer escolaId) {
        this.escola = escolaId != null ? new Escolas(escolaId, null, null, null, null, null) : null;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}

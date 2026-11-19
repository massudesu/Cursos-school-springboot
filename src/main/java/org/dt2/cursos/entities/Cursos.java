package org.dt2.cursos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cursos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String descricao;
    private int cargaHoraria;
    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escolas escola;
    @JsonIgnore

    public Cursos(int id, String nome, String descricao, int cargaHoraria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
    }

    public Cursos(String nome, String descricao, int cargaHoraria) {
        this.nome = nome;
        this.descricao = descricao;
        this.cargaHoraria = cargaHoraria;
    }
    public Cursos(){}

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
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
}

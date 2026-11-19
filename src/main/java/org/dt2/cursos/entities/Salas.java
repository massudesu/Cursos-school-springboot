package org.dt2.cursos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Salas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private int capacidade;
    private String localizacao;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escolas escola;

    public Salas(int id, String nome, int capacidade, String localizacao) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
    }

    public Salas(String nome, int capacidade, String localizacao) {
        this.nome = nome;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
    }
    public Salas(){}

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
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

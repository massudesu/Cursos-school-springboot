package org.dt2.cursos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Professores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String especialidade;
    @ManyToOne
    @JoinColumn(name = "escola_id")
    private Escolas escola;

    public Professores(int id, String nome, String email, String telefone, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.especialidade = especialidade;
    }

    public Professores(String nome, String email, String telefone, String especialidade) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.especialidade = especialidade;
    }
    public Professores(){}

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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
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
}

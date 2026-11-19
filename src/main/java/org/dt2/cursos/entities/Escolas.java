package org.dt2.cursos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Escolas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String endereco;

    @JsonIgnore
    @OneToMany(mappedBy = "escola")
    private List<Alunos> alunos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "escola")
    private List<Professores> professores = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "escola")
    private List<Cursos> cursos = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "escola")
    private List<Salas> salas = new ArrayList<>();

    public Escolas(int id, String nome, String cnpj, String email, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Escolas(String nome, String cnpj, String email, String telefone, String endereco) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Escolas(){}

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

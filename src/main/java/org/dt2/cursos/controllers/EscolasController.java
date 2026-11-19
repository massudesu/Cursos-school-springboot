package org.dt2.cursos.controllers;
import org.dt2.cursos.entities.Escolas;
import org.dt2.cursos.repositories.EscolasRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/escolas")
public class EscolasController {
    private final EscolasRepository repository;
    public EscolasController(EscolasRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<Escolas> mostrar(){
        return repository.findAll();
    }

    @PostMapping
    public String criarEscolas(@RequestBody Escolas esc){
        try {
            repository.save(esc);
            return "Salvo com sucesso";
        }
        catch (Exception e){
            return "Dados incorretos";
        }
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
}

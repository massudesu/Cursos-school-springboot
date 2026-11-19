package org.dt2.cursos.repositories;

import org.dt2.cursos.entities.Alunos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunosRepository extends JpaRepository<Alunos, Integer> {
}

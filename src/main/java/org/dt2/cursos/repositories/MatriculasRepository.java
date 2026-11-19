package org.dt2.cursos.repositories;

import org.dt2.cursos.entities.Matriculas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculasRepository extends JpaRepository<Matriculas, Long> {
}

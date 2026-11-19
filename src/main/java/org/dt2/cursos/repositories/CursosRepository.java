package org.dt2.cursos.repositories;

import org.dt2.cursos.entities.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursosRepository extends JpaRepository<Cursos, Integer> {
}

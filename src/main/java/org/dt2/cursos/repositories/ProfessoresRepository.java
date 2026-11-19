package org.dt2.cursos.repositories;

import org.dt2.cursos.entities.Professores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessoresRepository extends JpaRepository<Professores, Integer> {
}

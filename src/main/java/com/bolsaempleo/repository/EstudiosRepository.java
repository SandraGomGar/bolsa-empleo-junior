package com.bolsaempleo.repository;

import com.bolsaempleo.model.Estudios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudiosRepository extends JpaRepository<Estudios, Long> {
    // Puedes añadir métodos personalizados si lo necesitas más adelante
}

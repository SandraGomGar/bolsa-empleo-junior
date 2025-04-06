package com.bolsaempleo.repository;

import com.bolsaempleo.model.Oferta; // Importa desde tu paquete "model"
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    // ¡Listo! Spring genera automáticamente los métodos CRUD (save, findAll, etc.).
}

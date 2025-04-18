package com.bolsaempleo.repository;

import com.bolsaempleo.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
    // Buscar todas las ofertas creadas por una empresa específica
    List<Oferta> findByEmpresaId(Long empresaId);
}